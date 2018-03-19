/**
 * Created by cly on 2017/12/21 0027.
 * 设置iframe高度
 */
var indexModel = (function () {
    return {
        initFrameHeight : function () {
            var window_height = $(window).height();
            var header_height = $('.layui-header').outerHeight();
            var footer_height = $('.layui-footer').outerHeight();
            $("#content").height(window_height - header_height - footer_height-10);
        }

    }
})();

(function () {
    indexModel.initFrameHeight();
})();