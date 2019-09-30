layui.use(["form", "okLayer", "jquery", "layer"], function () {
    let form = layui.form;
    let $ = layui.jquery;

    let entryType_index = $('#businessType_index_tmp').val();
    let detail_entryType ='';
    switch (entryType_index) {
        case 'qz':
            detail_entryType = '整站业务';
            break;
        case 'pt':
            detail_entryType = '单词业务';
            break;
        case 'fm':
            detail_entryType = '负面业务';
            break;
    }
    $('#entryType_index').text(detail_entryType);

    $.ajax({
        url: '/internal/common/getKeywordTypeByUserRole',
        dataType: 'json',
        async: false,
        type: 'get',
        success: function (res) {
            if (res.code === 200) {
                $.each(res.data, function (index, item) {
                    let businessItem = item.split("#");
                    $('#businessInfo').append('<dd><a href="javascript:void(0)" onclick=changeBusinessType("'+businessItem[0]+'")>'+businessItem[1]+'</a></dd>');
                });
            }
        }
    });

    
    window.changeBusinessType = function (businessType) {
        $.ajax({
            url: '/changeBusinessType',
            data:{"businessType":businessType},
            dataType: 'json',
            async: false,
            type: 'post',
            success: function (res) {
                console.log(res);
                if (res.code === 200) {
                    top.location.reload();
                }
            }
        });
    }



});
