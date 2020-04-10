package com.cn.config;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.commons.beanutils.PropertyUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.ElasticsearchException;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.ScriptedField;
import org.springframework.data.elasticsearch.core.AbstractResultMapper;
import org.springframework.data.elasticsearch.core.DefaultEntityMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 扩展DefaultResultMapper实现高亮查询
 * @author ngcly
 */
@Component
public class HighLightResultMapper extends AbstractResultMapper {

    private final MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext;

    public HighLightResultMapper() {
        this(new SimpleElasticsearchMappingContext());
    }

    public HighLightResultMapper(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext) {
        super(new DefaultEntityMapper(mappingContext));
        Assert.notNull(mappingContext, "MappingContext must not be null!");
        this.mappingContext = mappingContext;
    }

    public HighLightResultMapper(EntityMapper entityMapper) {
        this(new SimpleElasticsearchMappingContext(), entityMapper);
    }

    public HighLightResultMapper(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext, EntityMapper entityMapper) {
        super(entityMapper);
        Assert.notNull(mappingContext, "MappingContext must not be null!");
        this.mappingContext = mappingContext;
    }

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        long totalHits = response.getHits().getTotalHits();
        float maxScore = response.getHits().getMaxScore();
        List<T> results = new ArrayList();
        Iterator var8 = response.getHits().iterator();
        while(var8.hasNext()) {
            SearchHit hit = (SearchHit)var8.next();
            if (hit != null) {
                T result;
                if (!StringUtils.isEmpty(hit.getSourceAsString())) {
                    result = this.mapEntity(hit.getSourceAsString(), clazz);
                } else {
                    result = this.mapEntity(hit.getFields().values(), clazz);
                }
                this.setPersistentEntityId(result, hit.getId(), clazz);
                this.setPersistentEntityVersion(result, hit.getVersion(), clazz);
                this.setPersistentEntityScore(result, hit.getScore(), clazz);
                this.populateScriptFields(result, hit);
                //高亮设置
                this.populateHighLightedFields(result, hit.getHighlightFields());
                results.add(result);
            }
        }

        return new AggregatedPageImpl(results, pageable, totalHits, response.getAggregations(), response.getScrollId(), maxScore);
    }

    /**
     * 使用beanutils工具给对象的属性赋值
     *
     * @param result
     * @param highlightFields
     * @param <T>
     */
    private <T>  void populateHighLightedFields(T result, Map<String, HighlightField> highlightFields) {
        for (HighlightField field : highlightFields.values()) {
            try {
                PropertyUtils.setProperty(result, field.getName(), textToStr(field.fragments()));
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new ElasticsearchException("failed to set highlighted value for field: " + field.getName()
                        + " with value: " + Arrays.toString(field.getFragments()), e);
            }
        }
    }

    /**
     * 将Text[]转化为String
     *
     * @param texts
     * @return
     */
    private String textToStr(Text[] texts) {
        StringBuffer sb = new StringBuffer();
        for (Text text : texts) {
            sb.append(text.toString());
        }
        return sb.toString();
    }


    private <T> void populateScriptFields(T result, SearchHit hit) {
        if (hit.getFields() != null && !hit.getFields().isEmpty() && result != null) {
            Field[] var3 = result.getClass().getDeclaredFields();
            int var4 = var3.length;
            for(int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                ScriptedField scriptedField = field.getAnnotation(ScriptedField.class);
                if (scriptedField != null) {
                    String name = scriptedField.name().isEmpty() ? field.getName() : scriptedField.name();
                    DocumentField searchHitField = hit.getFields().get(name);
                    if (searchHitField != null) {
                        field.setAccessible(true);
                        try {
                            field.set(result, searchHitField.getValue());
                        } catch (IllegalArgumentException var11) {
                            throw new ElasticsearchException("failed to set scripted field: " + name + " with value: " + searchHitField.getValue(), var11);
                        } catch (IllegalAccessException var12) {
                            throw new ElasticsearchException("failed to access scripted field: " + name, var12);
                        }
                    }
                }
            }
        }

    }

    private <T> T mapEntity(Collection<DocumentField> values, Class<T> clazz) {
        return this.mapEntity(this.buildJSONFromFields(values), clazz);
    }

    private String buildJSONFromFields(Collection<DocumentField> values) {
        JsonFactory nodeFactory = new JsonFactory();
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            JsonGenerator generator = nodeFactory.createGenerator(stream, JsonEncoding.UTF8);
            generator.writeStartObject();
            Iterator var5 = values.iterator();
            while(true) {
                while(var5.hasNext()) {
                    DocumentField value = (DocumentField)var5.next();
                    if (value.getValues().size() > 1) {
                        generator.writeArrayFieldStart(value.getName());
                        Iterator var7 = value.getValues().iterator();
                        while(var7.hasNext()) {
                            Object val = var7.next();
                            generator.writeObject(val);
                        }
                        generator.writeEndArray();
                    } else {
                        generator.writeObjectField(value.getName(), value.getValue());
                    }
                }
                generator.writeEndObject();
                generator.flush();
                return new String(stream.toByteArray(), Charset.forName("UTF-8"));
            }
        } catch (IOException var9) {
            return null;
        }
    }

    @Override
    public <T> T mapResult(GetResponse response, Class<T> clazz) {
        T result = this.mapEntity(response.getSourceAsString(), clazz);
        if (result != null) {
            this.setPersistentEntityId(result, response.getId(), clazz);
            this.setPersistentEntityVersion(result, response.getVersion(), clazz);
        }

        return result;
    }

    @Override
    public <T> LinkedList<T> mapResults(MultiGetResponse responses, Class<T> clazz) {
        LinkedList<T> list = new LinkedList();
        MultiGetItemResponse[] var4 = responses.getResponses();
        int var5 = var4.length;
        for(int var6 = 0; var6 < var5; ++var6) {
            MultiGetItemResponse response = var4[var6];
            if (!response.isFailed() && response.getResponse().isExists()) {
                T result = this.mapEntity(response.getResponse().getSourceAsString(), clazz);
                this.setPersistentEntityId(result, response.getResponse().getId(), clazz);
                this.setPersistentEntityVersion(result, response.getResponse().getVersion(), clazz);
                list.add(result);
            }
        }
        return list;
    }

    private <T> void setPersistentEntityId(T result, String id, Class<T> clazz) {
        if (clazz.isAnnotationPresent(Document.class)) {
            ElasticsearchPersistentEntity<?> persistentEntity = this.mappingContext.getRequiredPersistentEntity(clazz);
            ElasticsearchPersistentProperty idProperty = persistentEntity.getIdProperty();
            if (idProperty != null && idProperty.getType().isAssignableFrom(String.class)) {
                persistentEntity.getPropertyAccessor(result).setProperty(idProperty, id);
            }
        }

    }

    private <T> void setPersistentEntityVersion(T result, long version, Class<T> clazz) {
        if (clazz.isAnnotationPresent(Document.class)) {
            ElasticsearchPersistentEntity<?> persistentEntity = (ElasticsearchPersistentEntity)this.mappingContext.getPersistentEntity(clazz);
            ElasticsearchPersistentProperty versionProperty = persistentEntity.getVersionProperty();
            if (versionProperty != null && versionProperty.getType().isAssignableFrom(Long.class)) {
                Assert.isTrue(version != -1L, "Version in response is -1");
                persistentEntity.getPropertyAccessor(result).setProperty(versionProperty, version);
            }
        }

    }

    private <T> void setPersistentEntityScore(T result, float score, Class<T> clazz) {
        if (clazz.isAnnotationPresent(Document.class)) {
            ElasticsearchPersistentEntity<?> entity = this.mappingContext.getRequiredPersistentEntity(clazz);
            if (!entity.hasScoreProperty()) {
                return;
            }
            entity.getPropertyAccessor(result).setProperty(entity.getScoreProperty(), score);
        }
    }
}
