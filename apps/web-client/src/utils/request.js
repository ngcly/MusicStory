import service from './service';

//将接口配置的数组对象转化为方法
const request = apiConfig => {
    const serviceMap = {};
    apiConfig.map(({name,url,method,headers}) => {
        serviceMap[name] = async function(data={},rest='') {
            let key = "params";
            if(method === "post" || method === "put") {
                key = "data";
            }
            return service({
                method,
                headers,
                url: url+rest,
                [key]:data
            });
        };
    });
    return serviceMap;
};

export default request;