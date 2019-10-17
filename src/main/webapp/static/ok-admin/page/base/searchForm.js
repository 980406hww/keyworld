let open = true;

function showCondition() {
    let searchContent = document.getElementById('searchContent');
    if (open) {
        searchContent.style.display = 'block';
    } else {
        searchContent.style.display = 'none';
    }
    open = !open;
}

layui.use(['jquery'], function () {
    var $ = layui.jquery;
    window.formToJsonObject =function (form_id) {
        var formData = decodeURIComponent($("#" + form_id).serialize(), true);
        formData = formData.replace(/&/g, "\",\"");
        formData = formData.replace(/=/g, "\":\"");
        formData = "{\"" + formData + "\"}";
        formData = $.parseJSON(formData);
        $.each(formData,function(idx,item){
            formData[idx] = $.trim(item)
        });
        return formData;
    }
});