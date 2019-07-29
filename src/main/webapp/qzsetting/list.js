$(function () {
    $("#chargeLogListDiv").dialog("close");
    $("#chargeDialog").dialog("close");
    $("#changeSettingDialog").dialog("close");
    $("#getAvailableQZSettings").dialog("close");
    $("#showAllOperationType").dialog("close");
    $("#customerKeywordDialog").dialog("close");
    $("#showUserMessageDialog").dialog("close");
    $("#excludeCustomerKeywordDialog").dialog("close");

    var searchCustomerForm = $("#chargeForm");
    var pageSize = searchCustomerForm.find('#pageSizeHidden').val();
    var pages = searchCustomerForm.find('#pagesHidden').val();
    $("#chargeForm").find("#status").val($("#statusHidden").val());
    var currentPageNumber = searchCustomerForm.find('#currentPageNumberHidden').val();
    var showCustomerBottomDiv = $('#showCustomerBottomDiv');
    showCustomerBottomDiv.find('#chooseRecords').val(pageSize);
    showCustomerBottomDiv.find('#pagesHidden').val(pages);

    if(parseInt(currentPageNumber) > 1 && parseInt(currentPageNumber) < parseInt(pages)) {
        showCustomerBottomDiv.find("#firstButton").removeAttr("disabled");
        showCustomerBottomDiv.find("#upButton").removeAttr("disabled");
        showCustomerBottomDiv.find("#nextButton").removeAttr("disabled");
        showCustomerBottomDiv.find("#lastButton").removeAttr("disabled");
    } else if (parseInt(pages) <= 1) {
        showCustomerBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#upButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#nextButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#lastButton").attr("disabled", "disabled");
    } else if (parseInt(currentPageNumber) <= 1) {
        showCustomerBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#upButton").attr("disabled", "disabled");
    } else {
        showCustomerBottomDiv.find("#nextButton").attr("disabled", "disabled");
        showCustomerBottomDiv.find("#lastButton").attr("disabled", "disabled");
    }
    loadingRankInfo();
    searchRiseOrFall();
    detectedMoreSearchConditionDivShow();
});
function enterIn(e) {
    var e = e || event,
        keyCode = e.which || e.keyCode;
    if (keyCode == 13) {
        trimSearchCondition('1');
    }
}
function loadingRankInfo() {
    setTimeout(function (){
        detectedTopNum();
    }, 200);
    setTimeout(function () {
        var terminalType = $("#chargeForm").find("#terminalType").val();
        getQZSettingGroupInfo(terminalType);
    }, 100);
}
function searchRiseOrFall() {
    var checkedRadio = $(".mytabs div:eq(0)").find("input:radio[checked='checked']");
    $(".mytabs div:eq(0)").find("input:radio").click(function () {
        var checkStatus;
        var parentName = $(this).parent().attr("name");
        if (checkedRadio.length > 0 && checkedRadio.parent().attr("name") === parentName) {
            checkStatus = null;
        } else {
            switch (parentName) {
                case 'lower':
                    checkStatus = 1;
                    break;
                case 'upper':
                    checkStatus = 2;
                    break;
                case "atLeastStandard":
                    checkStatus = 3;
                    break;
                case 'neverStandard':
                    checkStatus = 4;
                    break;
                case 'closeStandard':
                    checkStatus = 5;
                    break;
                case "unchanged":
                    checkStatus = 6;
                    break;
                case 'lowerDifference':
                    checkStatus = 7;
                    break;
                case 'unchangedDifference':
                    checkStatus = 8;
                    break;
                case 'upperDifference':
                    checkStatus = 9;
                    break;
                default:
                    break;
            }
        }
        $("#chargeForm").find("#checkStatus").val(checkStatus);
        $(".mytabs div:eq(0)").find("input:radio").each(function() {
            if ($(this).parent().attr("name") != parentName) {
                $(this).prop("checked", false);
            }
        });
        trimSearchCondition('1');
    });
}
function detectedMoreSearchConditionDivShow() {
    var moreSearchCondition = $("div.conn[name='moreSearchCondition']");
    var customerInfo = moreSearchCondition.find("ul li.customerInfo input").val();
    var categoryTag = moreSearchCondition.find("ul li.category input").val();
    var group =  moreSearchCondition.find("ul li.group input").val();
    var operationType = moreSearchCondition.find("select[name='operationType']").val();
    var status = moreSearchCondition.find("select[name='status']").val();
    var renewalStatus = moreSearchCondition.find("select[name='renewalStatus']").val();
    var standardSpecies = moreSearchCondition.find("select[name='standardSpecies']").val();
    var optimizationType = moreSearchCondition.find("select[name='optimizationType']").val();
    var updateStatus = moreSearchCondition.find("select[name='updateStatus']").val();
    var createTime = moreSearchCondition.find("ul li.createTime input[name='createTime']").val();
    var createTimePrefix = moreSearchCondition.find("ul li.createTime input[name='createTimePrefix']").val();
    var hasMonitor = moreSearchCondition.find("select[name='hasMonitor']").val();
    if (hasMonitor === undefined) {
        hasMonitor = "";
    }
    var hasReady = moreSearchCondition.find("select[name='hasReady']").val();
    var userInfoID = $("#chargeForm").find("#userInfoID").val();
    var organizationID = $("#chargeForm").find("#organizationID").val();
    var hasText = false;
    var treeValue;
    if (userInfoID !== "") {
        hasText = true;
        treeValue = userInfoID;
    } else if (organizationID !== "") {
        hasText = true;
        treeValue = organizationID;
    }
    if (hasText) {
        $('#userNameTree').combotree({
            url : '/internal/user/tree',
            idFiled: 'id',
            treeField: 'name',
            parentField : 'pid',
            lines : true,
            panelHeight : 'auto'
        });
        $("#userNameTree").textbox('setValue', treeValue);
    }
    var values = customerInfo + categoryTag + group + status + renewalStatus + standardSpecies + optimizationType + updateStatus +
        createTime + createTimePrefix + operationType + hasMonitor + hasReady + userInfoID + organizationID;
    if (values !== "") {
        moreSearchCondition.css("display", "block");
    }
}
function checkTerminalType(searchEngine, terminalType) {
    var a = $(".mytabs .link").find("li.active a");
    var html = searchEngine;
    if (terminalType === 'PC') {
        html += terminalType;
    }
    if (a[0] !== undefined && a[0].innerHTML === html) {
        return;
    }
    $(".mytabs .link").find("li").removeClass("active");
    $(".mytabs .link").find("li[name='"+ html +"']").addClass("active");
    if (html.indexOf('A') > -1) {
        $("#chargeForm").find("#searchEngine").val('All');
    } else {
        $("#chargeForm").find("#searchEngine").val($.trim(html.substr(0, html.indexOf('P'))));
    }
    $("#chargeForm").find("#terminalType").val($.trim(terminalType));
    trimSearchCondition('1');
}
function detectedTopNum() {
    $(".body").find(".rank-wrap").each(function () {
        generateQZKeywordRecordCharts($(this).find("#keywordRecordCharts")[0], $(this).find("div[name='rankInfo'] span").text());
        generateQZKeywordTrendCharts($(this).find("#keywordTrendCharts")[0], $(this).find("div[name='rankInfo'] span").text());
        $(this).find(".row4").each(function () {
            var a = $(this).find("span:last-child a");
            if (a[0].innerHTML.trim() >= 0) {
                $(a).addClass("green");
            } else if (a[0].innerHTML.trim() < 0) {
                $(a).addClass("red");
            }
        });
    });
    $(".body").find(".rank-wrap1").each(function () {
        // 指定词曲线
        generateQZDesignationWordTrendCharts($(this).find("#designationWordCharts")[0], $(this).find("div[name='rankInfo'] span").text());
        $(this).find(".row4").each(function () {
            var a = $(this).find("span:last-child a");
            if (a[0].innerHTML.trim() >= 0) {
                $(a).addClass("green");
            } else if (a[0].innerHTML.trim() < 0) {
                $(a).addClass("red");
            }
        });
    });
}
function generateQZKeywordRecordCharts(domElement, data) {
    if (domElement === undefined) {
        return;
    }
    if (data === '' || JSON.parse(data).date === '') {
        domElement.innerHTML = "<h1 style='text-align: center'> 暂无数据 </h1>";
        return;
    }
    var result = JSON.parse(data);
    var date = result.baiduRecordFullDate.replace("['", "").replace("']", "").split("', '").reverse();
    var baiduRecord = result.baiduRecord.replace("['", "").replace("']", "").split("', '").reverse();
    var keywordRecordCharts = echarts.init(domElement);
    var option = {
        color: ['#0000FF'],
        title : {
            text: (result.websiteType === 'aiZhan' ? '爱站' : '5118') + ' - 百度收录趋势',
            textStyle: {
                color: '#999',
                fontFamily: "Arial",
                fontWeight: 400,
                fontSize: 12
            },
            x:'center',
            bottom: -3
        },
        tooltip: {
            trigger: 'axis'
        },
        grid: {
            left: '0%',
            right: '1%',
            top: '3%',
            bottom: '0%',
            containLabel: true
        },
        toolbox: {
            show: false,
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            show: false,
            type: 'category',
            axisLine: {
                show: false,
                lineStyle: {
                    color: '#404A59'
                }
            },
            axisTick: {
                show: false
            },
            boundaryGap: true,
            data: date
        },
        yAxis: {
            axisLine: {
                show: false,
                lineStyle: {
                    color: '#404A59'
                }
            },
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#DCDCDC',
                    width: 1,
                    type: 'solid'
                }
            },
            axisTick: {
                show: false
            },
            axisLabel: {
                fontStyle: 'italic',
                textStyle: {
                    color: '#000'
                }
            },
            type: 'value'
        },
        series: [{
            name: '收录',
            smooth: true,
            type: 'line',
            symbolSize: 1,
            symbol: 'none',
            data: baiduRecord,
            lineStyle:{
                type:"solid",
                width: 1
            }
        }]
    };
    keywordRecordCharts.setOption(option);
}
function generateQZKeywordTrendCharts(domElement, data) {
    if (domElement === undefined) {
        return;
    }
    if (data === '' || JSON.parse(data).date === '') {
        domElement.innerHTML = "<h1 style='text-align: center'> 暂无数据 </h1>";
        return;
    }
    var result = JSON.parse(data);
    var date = result.date.replace("['", "").replace("']", "").split("', '").reverse();
    var keywordTrendCharts = echarts.init(domElement);
    var option;
    var topTen = stringToArray(result.topTen);
    var topTwenty = stringToArray(result.topTwenty);
    var topFifty = stringToArray(result.topFifty);
    var topThirty;
    var topForty;
    var topHundred;
    var parentElement = $(domElement).parent()[0];
    $(parentElement).find("#" + result.terminalType + "Top10").text(topTen[topTen.length-1]);
    $(parentElement).find("#" + result.terminalType + "Top50").text(topFifty[topFifty.length-1]);
    $(parentElement).find("#" + result.terminalType + "TopCreate10").text(result.createTopTenNum);
    $(parentElement).find("#" + result.terminalType + "TopCreate50").text(result.createTopFiftyNum);
    if (result.dataProcessingStatus) {
        $(parentElement).find("#" + result.terminalType + "IsStandard").text(result.achieveLevel === 0 ? "否" : "是");
        $(parentElement).find("#" + result.terminalType + "StandardTime").text(result.achieveTime === null ? "无" : toDateFormat(new Date(result.achieveTime.time)));
    } else {
        $(parentElement).find("#" + result.terminalType + "IsStandard").parent().parent().css("display", "none");
        $(parentElement).find("#" + result.terminalType + "StandardTime").parent().parent().css("display", "none");
    }
    if (result.websiteType === "aiZhan") {
        topThirty = stringToArray(result.topThirty);
        topForty = stringToArray(result.topForty);
        option = {
            color: ['#228B22', '#0000FF', '#FF6100', '#000000', '#FF0000'],
            title : {
                text: '爱站 - 关键词排名趋势',
                textStyle: {
                    color: '#999',
                    fontFamily: "Arial",
                    fontWeight: 400,
                    fontSize: 12
                },
                x:'center',
                bottom: -3
            },
            tooltip: {
                trigger: 'axis'
            },
            grid: {
                left: '0%',
                right: '1%',
                top: '3%',
                bottom: '0%',
                containLabel: true
            },
            toolbox: {
                show: false,
                feature: {
                    saveAsImage: {}
                }
            },
            xAxis: {
                show: false,
                type: 'category',
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#404A59'
                    }
                },
                axisTick: {
                    show: false
                },
                boundaryGap: true,
                data: date
            },
            yAxis: {
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#404A59'
                    }
                },
                splitLine: {
                    show: true,
                    lineStyle: {
                        color: '#DCDCDC',
                        width: 1,
                        type: 'solid'
                    }
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    fontStyle: 'italic',
                    textStyle: {
                        color: '#000'
                    }
                },
                type: 'value'
            },
            series: [{
                name: '前10名',
                smooth: true,
                type: 'line',
                symbolSize: 1,
                symbol: 'none',
                data: topTen,
                lineStyle:{
                    type:"solid",
                    width: 1
                }
            }, {
                name: '前20名',
                smooth: true,
                type: 'line',
                symbolSize: 1,
                symbol: 'none',
                data: topTwenty,
                lineStyle:{
                    type:"solid",
                    width: 1
                }
            }, {
                name: '前30名',
                smooth: true,
                type: 'line',
                symbolSize: 1,
                symbol: 'none',
                data: topThirty,
                lineStyle:{
                    type:"solid",
                    width: 1
                }
            }, {
                name: '前40名',
                smooth: true,
                type: 'line',
                symbolSize: 1,
                symbol: 'none',
                data: topForty,
                lineStyle:{
                    type:"solid",
                    width: 1
                }
            }, {
                name: '前50名',
                smooth: true,
                type: 'line',
                symbolSize: 1,
                symbol: 'none',
                data: topFifty,
                lineStyle:{
                    type:"solid",
                    width: 1
                }
            }]
        };
    } else if (result.websiteType === "5118") {
        topHundred = stringToArray(result.topHundred);
        option = {
            color: ['#228B22', '#0000FF', '#FF6100', '#FF0000'],
            title : {
                text: result.websiteType + ' - 关键词排名趋势',
                textStyle: {
                    color: '#999',
                    fontFamily: "Arial",
                    fontWeight: 400,
                    fontSize: 12
                },
                x:'center',
                bottom: -3
            },
            tooltip: {
                trigger: 'axis'
            },
            grid: {
                left: '0%',
                right: '1%',
                top: '3%',
                bottom: '0%',
                containLabel: true
            },
            toolbox: {
                show: false,
                feature: {
                    saveAsImage: {}
                }
            },
            xAxis: {
                show: false,
                type: 'category',
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#404A59'
                    }
                },
                axisTick: {
                    show: false
                },
                boundaryGap: true,
                data: date
            },
            yAxis: {
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#404A59'
                    }
                },
                splitLine: {
                    show: true,
                    lineStyle: {
                        color: '#DCDCDC',
                        width: 1,
                        type: 'solid'
                    }
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    fontStyle: 'italic',
                    textStyle: {
                        color: '#000'
                    }
                },
                type: 'value'
            },
            series: [{
                name: '前10名',
                smooth: true,
                type: 'line',
                symbolSize: 1,
                symbol: 'none',
                data: topTen,
                lineStyle:{
                    type:"solid",
                    width: 1
                }
            }, {
                name: '前20名',
                smooth: true,
                type: 'line',
                symbolSize: 1,
                symbol: 'none',
                data: topTwenty,
                lineStyle:{
                    type:"solid",
                    width: 1
                }
            }, {
                name: '前50名',
                smooth: true,
                type: 'line',
                symbolSize: 1,
                symbol: 'none',
                data: topFifty,
                lineStyle:{
                    type:"solid",
                    width: 1
                }
            }, {
                name: '前100名',
                smooth: true,
                type: 'line',
                symbolSize: 1,
                symbol: 'none',
                data: topHundred,
                lineStyle:{
                    type:"solid",
                    width: 1
                }
            }]
        };
    }
    keywordTrendCharts.setOption(option);
}
function generateQZDesignationWordTrendCharts(domElement, data) {
    if (domElement === undefined ) {
        return;
    }
    if (data === '' || JSON.parse(data).date === '') {
        domElement.innerHTML = "<h1 style='text-align: center'> 暂无数据 </h1>";
        return;
    }
    if ($.trim($(domElement).parent().parent().find("a[name='fIsMonitor']").text()) === "否") {
        domElement.innerHTML = "<h1 style='text-align: center'> 请进行达标监控 </h1>";
        return;
    }
    var result = JSON.parse(data);
    var date = result.date.replace("['", "").replace("']", "").split("', '").reverse();
    var designationWordTrendCharts = echarts.init(domElement);
    var option;
    var topTen = stringToArray(result.topTen);
    var topTwenty = stringToArray(result.topTwenty);
    var topFifty = stringToArray(result.topFifty);
    var topThirty = stringToArray(result.topThirty);
    var topForty = stringToArray(result.topForty);
    var parentElement = $(domElement).parent()[0];
    $(parentElement).find("#" + result.terminalType + "Top10").text(topTen[topTen.length-1]);
    $(parentElement).find("#" + result.terminalType + "Top50").text(topFifty[topFifty.length-1]);
    $(parentElement).find("#" + result.terminalType + "TopCreate10").text(result.createTopTenNum);
    $(parentElement).find("#" + result.terminalType + "TopCreate50").text(result.createTopFiftyNum);
    $(parentElement).find("#" + result.terminalType + "IsStandard").text(result.achieveLevel === 0 ? "否" : "是");
    $(parentElement).find("#" + result.terminalType + "StandardTime").text(result.achieveTime === null ? "无" : toDateFormat(new Date(result.achieveTime.time)));
    option = {
        color: ['#228B22', '#0000FF', '#FF6100', '#000000', '#FF0000'],
        title : {
            text: '指定词排名趋势',
            textStyle: {
                color: '#999',
                fontFamily: "Arial",
                fontWeight: 400,
                fontSize: 12
            },
            x:'center',
            bottom: -3
        },
        tooltip: {
            trigger: 'axis'
        },
        grid: {
            left: '0%',
            right: '1%',
            top: '3%',
            bottom: '0%',
            containLabel: true
        },
        toolbox: {
            show: false,
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            show: false,
            type: 'category',
            axisLine: {
                show: false,
                lineStyle: {
                    color: '#404A59'
                }
            },
            axisTick: {
                show: false
            },
            boundaryGap: true,
            data: date
        },
        yAxis: {
            axisLine: {
                show: false,
                lineStyle: {
                    color: '#404A59'
                }
            },
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#DCDCDC',
                    width: 1,
                    type: 'solid'
                }
            },
            axisTick: {
                show: false
            },
            axisLabel: {
                fontStyle: 'italic',
                textStyle: {
                    color: '#000'
                }
            },
            type: 'value'
        },
        series: [{
            name: '前10名',
            smooth: true,
            type: 'line',
            symbolSize: 1,
            symbol: 'none',
            data: topTen,
            lineStyle:{
                type:"solid",
                width: 1
            }
        }, {
            name: '前20名',
            smooth: true,
            type: 'line',
            symbolSize: 1,
            symbol: 'none',
            data: topTwenty,
            lineStyle:{
                type:"solid",
                width: 1
            }
        }, {
            name: '前30名',
            smooth: true,
            type: 'line',
            symbolSize: 1,
            symbol: 'none',
            data: topThirty,
            lineStyle:{
                type:"solid",
                width: 1
            }
        }, {
            name: '前40名',
            smooth: true,
            type: 'line',
            symbolSize: 1,
            symbol: 'none',
            data: topForty,
            lineStyle:{
                type:"solid",
                width: 1
            }
        }, {
            name: '前50名',
            smooth: true,
            type: 'line',
            symbolSize: 1,
            symbol: 'none',
            data: topFifty,
            lineStyle:{
                type:"solid",
                width: 1
            }
        }]
    };
    designationWordTrendCharts.setOption(option);
}
function stringToArray(str) {
    return str.replace('[', '').replace(']', '').split(', ').reverse();
}
function getQZSettingGroupInfo(terminalType) {
    $(".body").find(".other-rank_2").each(function () {
        var div = $(this);
        var uuid = div.parent().parent().parent().find(".header input[name='uuid']").val();
        var optimizeGroupName = div.find(".row:first-child").find("div:eq(0) span.line1 a").text();
        if (optimizeGroupName.indexOf("(") === -1) {
            optimizeGroupName = $.trim(optimizeGroupName);
        } else {
            optimizeGroupName = $.trim(optimizeGroupName.substring(0,optimizeGroupName.indexOf("(")));
        }
        var postData = {};
        postData.qzSettingUuid = uuid;
        postData.terminalType = terminalType;
        postData.optimizeGroupName = optimizeGroupName;
        $.ajax({
            url: '/internal/qzsetting/getQZSettingGroupInfo',
            type: 'POST',
            data: JSON.stringify(postData),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (data) {
                div.parent().find(".other-rank .row:first-child").find("div[name='operationKeywordNum']").find("span.line1 a").text(data.customerKeywordCount);
                if (data.categoryTagNames.length > 0) {
                    var tagNameStr = "";
                    var span = $(div).parent().parent().parent().find(".header span.tagNames");
                    $.each(data.categoryTagNames, function (idx, val) {
                        console.log(val)
                        tagNameStr += val + ",";
                    });
                    console.log("======================")
                    $(span).find("label.tagNameStr").html(tagNameStr.substring(0, tagNameStr.length-1));
                }

                if (data.operationCombineName !== null) {
                    div.find("select[name='operationCombineName'] option:not(:first)").each(function () {
                        if ($(this).val().split("_____")[0] === data.operationCombineName) {
                            $(this)[0].selected = true;
                            div.find("input[name='operationCombineName']").val($(this).val());
                        }
                    });
                }
                div.find(".row:first-child").find("div:eq(0) span.line1 a").text(optimizeGroupName+" ("+ (data.machineCount == null ? 0 : data.machineCount) +")");
            },
            error: function () {
                $().toastmessage('showErrorToast', '获取优化分组机器信息失败，请刷新重试或提交问题给开发人员！');
            }
        });
    });
}
function editTagNameStr(o, edit){
    if (edit) {
        o.innerHTML = o.innerHTML.replace(/(暂无)/g, '');
        var uuid = $(o).parent().parent().find("input[name='uuid']").val();
        o.innerHTML = '<input type="text" label="'+ o.innerHTML +'" uuid="'+ uuid +'" style="width: 250px;" value="' + o.innerHTML + '" onblur="editTagNameStr(this)">';
        o.getElementsByTagName('input')[0].focus();
    } else {
        var isChange = true;
        var qzCategoryTags = [];
        var categoryTagNames = o.value.replace(/( )+/g,"").replace(/(，)+|(,)+/g, ",").split(",");
        categoryTagNames = unique(categoryTagNames);
        if (o.value !== "") {
            o.value = "";
            $.each(categoryTagNames, function (idx, val) {
                if (val !== "") {
                    var qzCategoryTag = {};
                    qzCategoryTag.tagName = $.trim(val);
                    qzCategoryTags.push(qzCategoryTag);
                    o.value += val + ",";
                }
            });
            o.value = o.value.substring(0, o.value.length-1);
        }
        var label = $(o).attr("label");
        if ($.trim(o.value) === $.trim(label)) {
            isChange = false;
        }
        if (isChange) {
            var postData = {};
            var qzSettingUuid = $(o).attr("uuid");
            postData.qzSettingUuid = $.trim(qzSettingUuid);
            postData.qzCategoryTags = qzCategoryTags;
            $.ajax({
                url: "/internal/qzcategorytag/save",
                type: "POST",
                data: JSON.stringify(postData),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                success: function (data) {
                    if (data) {
                        $().toastmessage('showSuccessToast', "保存成功！");
                    } else {
                        $().toastmessage('showErrorToast', "保存失败！");
                        o.value = $.trim(label);
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "保存失败！");
                    o.value = $.trim(label);
                }
            });
        }
        setTimeout(function(){
            if ($.trim(o.value) === "") {
                o.value = "暂无";
            }
            o.parentNode.innerHTML = $.trim(o.value);
        }, 100);
    }
}
function unique(a) {
    var seen = {};
    return a.filter(function(item) {
        return seen.hasOwnProperty(item) ? false : (seen[item] = true);
    });
}
function trimSearchCondition(days) {
    var chargeForm = $("#chargeForm");
    var customerInfo = $(".conn").find(".customerInfo").find("input[name='customerInfo']").val();
    var resetPagingParam = false;
    var treeInput = $("#userNameTree").parent().find("input[name='userName']");
    var text = "";
    var id;
    if (treeInput.length > 0) {
        if (treeInput.val() !== "") {
            id = $("#userNameTree").textbox('getValue');
            text = $("#userNameTree").textbox('getText');
        }
    }
    if (text !== "") {
        if (text.indexOf('部') === text.length-1 || text.indexOf('办') === text.length-1) {
            chargeForm.find("#userInfoID").val(null);
            chargeForm.find("#organizationID").val(id);
        } else {
            chargeForm.find("#userInfoID").val(id);
            chargeForm.find("#organizationID").val(null);
        }
    } else {
        chargeForm.find("#userInfoID").val(null);
        chargeForm.find("#organizationID").val(null);
    }
    var customerUuid = customerInfo.substr(customerInfo.lastIndexOf("_") + 1);
    chargeForm.find("#customerInfo").val($.trim(customerInfo));
    chargeForm.find("#customerUuid").val(customerUuid);
    chargeForm.find("#dateRangeType").val(days);

    var domain = $(".conn").find("input[name='domain']").val();
    var categoryTag = $(".conn").find(".category").find("input[name='categoryTag']").val();
    var group = $(".conn").find(".group").find("input[name='group']").val();
    var operationType = $(".conn").find("select[name='operationType']").val();
    var status = $(".conn").find("select[name='status']").val();
    var renewalStatus = $(".conn").find("select[name='renewalStatus']").val();
    var standardSpecies = $(".conn").find("select[name='standardSpecies']").val();
    var optimizationType = $(".conn").find("select[name='optimizationType']").val();
    var updateStatus = $(".conn").find("select[name='updateStatus']").val();
    var createTime = $(".conn").find(".createTime").find("input[name='createTime']").val();
    var createTimePrefix = $(".conn").find(".createTime").find("input[name='createTimePrefix']").val();
    var hasMonitor = $(".conn li").find("select[name='hasMonitor']").val();;
    var hasReady = $(".conn li").find("select[name='hasReady']").val();;

    var str = text + customerUuid + domain + categoryTag + group + operationType + status + standardSpecies + optimizationType
        + updateStatus + createTime + createTimePrefix + hasMonitor + hasReady;
    if (str !== '') {
        resetPagingParam = true;
    }
    chargeForm.find("#resetPagingParam").val(resetPagingParam);
    chargeForm.find("#domain").val($.trim(domain));
    chargeForm.find("#categoryTag").val($.trim(categoryTag));
    chargeForm.find("#group").val($.trim(group));
    if (operationType !== ""){
        chargeForm.find("#operationType").val($.trim(operationType));
    } else {
        chargeForm.find("#operationType").val(null);
    }
    if (status !== "") {
        chargeForm.find("#status").val($.trim(status));
    } else {
        chargeForm.find("#status").val(null);
    }
    if (renewalStatus !== "") {
        chargeForm.find("#renewalStatus").val($.trim(renewalStatus));
    } else {
        chargeForm.find("#renewalStatus").val(null);
    }
    if (standardSpecies !== '') {
        chargeForm.find("#standardSpecies").val(standardSpecies);
    } else {
        chargeForm.find("#standardSpecies").val(null);
    }
    if (optimizationType !== '') {
        chargeForm.find("#optimizationType").val(optimizationType);
    } else {
        chargeForm.find("#optimizationType").val(null);
    }
    if (updateStatus !== "") {
        chargeForm.find("#updateStatus").val($.trim(updateStatus));
    } else {
        chargeForm.find("#updateStatus").val(null);
    }
    if (createTime !== "") {
        chargeForm.find("#createTime").val($.trim(createTime));
    } else {
        chargeForm.find("#createTime").val(null);
    }
    if (createTimePrefix !== "") {
        chargeForm.find("#createTimePrefix").val($.trim(createTimePrefix));
    } else {
        chargeForm.find("#createTimePrefix").val(null);
    }
    if (hasMonitor !== "" && hasMonitor !== undefined) {
        chargeForm.find("#hasMonitor").val(hasMonitor);
    } else {
        chargeForm.find("#hasMonitor").val(null);
    }
    if (hasReady !== "" && hasReady !== undefined) {
        chargeForm.find("#hasReady").val(hasReady);
    } else {
        chargeForm.find("#hasReady").val(null);
    }
    chargeForm.submit();
}
function showMoreSearchCondition() {
    $(".mytabs").find("div[name='moreSearchCondition']").toggle();
    if ($(".mytabs").find("div[name='moreSearchCondition']").css("display") === "block") {
        $('#userNameTree').combotree({
            url : '/internal/user/tree',
            idFiled: 'id',
            treeField: 'name',
            parentField : 'pid',
            lines : true,
            panelHeight : 'auto'
        });
    }
}
function searchClientStatus(optimizeGroup) {
    var searchClientStatusFrom = $("#searchClientStatusForm");
    searchClientStatusFrom.find("#groupName").val($.trim(optimizeGroup));
    searchClientStatusFrom.submit();
}
function searchCustomerKeywords(customerUuid, optimizeGroupName) {
    var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
    searchCustomerKeywordForm.find("#customerUuid").val(customerUuid);
    searchCustomerKeywordForm.find("#optimizeGroupName").val(optimizeGroupName);
    searchCustomerKeywordForm.find("#status").val(1);
    searchCustomerKeywordForm.submit();
}
var TimeFn = null;
function showChargeRulesDiv(self, e) {
    var event = e||window.event;
    var pageX = event.pageX;
    var pageY = event.pageY;
    if(pageX==undefined) {
        pageX = event.clientX+document.body.scrollLeft||document.documentElement.scrollLeft;
    }
    if(pageY==undefined) {
        pageY = event.clientY+document.body.scrollTop||document.documentElement.scrollTop;
    }

    $("#chargeRulesDivTable tr").remove();
    clearTimeout(TimeFn);
    TimeFn = setTimeout(function(){
        var qzSettingUuid = $(self).attr("qzsettinguuid");
        var terminalType = $("#chargeForm").find("#terminalType").val();
        var postData = {};
        postData.qzSettingUuid = parseInt(qzSettingUuid);
        postData.terminalType = terminalType;
        $.ajax({
            url: '/internal/qzsetting/getChargeRule',
            type: 'POST',
            data: JSON.stringify(postData),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (result) {
                $("#chargeRulesDivTable tr").remove();
                if(result != null && result.qzChargeRuleMap != null) {
                    $("#chargeRulesDivTable").append("<tr>" +
                        "<td colspan='4'>达标类型: 满足全部</td>" +
                        "</tr>");
                    var qzChargeRules = {};
                    if (result.qzChargeRuleMap["aiZhan"] !== undefined) {
                        $("#chargeRulesDivTable").append("<tr>" +
                            "<td colspan='4'>"+ "爱站" +"</td>" +
                            "</tr>"+
                            "<tr>" +
                            "<td>序号</td>" +
                            "<td>起始词量</td>" +
                            "<td>终止词量</td>" +
                            "<td>价格</td>" +
                            "</tr>");
                        qzChargeRules = result.qzChargeRuleMap["aiZhan"];
                    } else if (result.qzChargeRuleMap["5118"] !== undefined) {
                        $("#chargeRulesDivTable").append("<tr>" +
                            "<td colspan='4'>"+ "5118" +"</td>" +
                            "</tr>"+
                            "<tr>" +
                            "<td>序号</td>" +
                            "<td>起始词量</td>" +
                            "<td>终止词量</td>" +
                            "<td>价格</td>" +
                            "</tr>");
                        qzChargeRules = result.qzChargeRuleMap["5118"];
                    }
                    $.each(qzChargeRules, function (idx, val) {
                        var newTr = document.createElement("tr");
                        var chargeRuleElements = [
                            idx + 1,
                            val.startKeywordCount,
                            val.endKeywordCount,
                            val.amount
                        ];
                        $.each(chargeRuleElements, function (index, v) {
                            var newTd = document.createElement("td");
                            newTr.appendChild(newTd);
                            if (v == null) {
                                newTd.innerHTML = "";
                            } else {
                                newTd.innerHTML = v;
                            }
                        });
                        if (idx + 1 === parseInt(val.achieveLevel)) {
                            $(newTr).css("background-color", "mediumseagreen");
                        }
                        $("#chargeRulesDivTable")[0].lastChild.appendChild(newTr);
                    });
                    if (result.qzChargeRuleMap["designationWord"] !== undefined) {
                        $("#chargeRulesDivTable").append("<tr>" +
                            "<td colspan='4'>"+ "指定词" +"</td>" +
                            "</tr>"+
                            "<tr>" +
                            "<td>序号</td>" +
                            "<td>起始词量</td>" +
                            "<td>终止词量</td>" +
                            "<td>价格</td>" +
                            "</tr>");
                        qzChargeRules = result.qzChargeRuleMap["designationWord"];
                    } else {
                        qzChargeRules = {};
                    }
                    $.each(qzChargeRules, function (idx, val) {
                        var newTr = document.createElement("tr");
                        var chargeRuleElements = [
                            idx + 1,
                            val.startKeywordCount,
                            val.endKeywordCount,
                            val.amount
                        ];
                        $.each(chargeRuleElements, function (index, v) {
                            var newTd = document.createElement("td");
                            newTr.appendChild(newTd);
                            if (v == null) {
                                newTd.innerHTML = "";
                            } else {
                                newTd.innerHTML = v;
                            }
                        });
                        if (idx + 1 === parseInt(val.achieveLevel)) {
                            $(newTr).css("background-color", "mediumseagreen");
                        }
                        $("#chargeRulesDivTable")[0].lastChild.appendChild(newTr);
                    });
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "获取信息失败！");
            }
        });
        var chargeRulesDiv = document.getElementById('chargeRulesDiv');
        chargeRulesDiv.style.display="block";
        chargeRulesDiv.style.left=(pageX) + "px";
        chargeRulesDiv.style.top=(pageY) + "px";
        chargeRulesDiv.style.zIndex=1000;
        chargeRulesDiv.style.position="absolute";
    }, 300);
}
function closeChargeRulesDiv() {
    clearTimeout(TimeFn);
    $("#chargeRulesDiv").css("display", "none");
}

function showKeywordDialog(qzSettingUuid, customerUuid, domain, optimizeGroupName, bearPawNumber) {
    var customerKeywordDialog = $("#customerKeywordDialog");
    customerKeywordDialog.find('#customerKeywordForm')[0].reset();
    customerKeywordDialog.find("#qzSettingUuid").val(qzSettingUuid);
    customerKeywordDialog.find("#customerUuid").val(customerUuid);
    customerKeywordDialog.find("#domain").val(domain);
    customerKeywordDialog.find("#optimizeGroupName").val(optimizeGroupName);
    customerKeywordDialog.find("#bearPawNumber").val(bearPawNumber);
    customerKeywordDialog.show();
    customerKeywordDialog.dialog({
        resizable: false,
        height: 470,
        width: 340,
        title: '指定关键字',
        modal: false,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveCustomerKeywords(qzSettingUuid, customerUuid, optimizeGroupName);
            }
        }, {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $("#customerKeywordDialog").dialog("close");
                $('#customerKeywordForm')[0].reset();
            }
        }]
    });
    customerKeywordDialog.dialog("open");
    customerKeywordDialog.window("resize", {
        top: $(document).scrollTop() + 150,
        left: $(document).scrollLeft() + $(window).width() / 2 - 170
    });
}
function saveCustomerKeywords(qzSettingUuid, customerUuid, tempOptimizeGroupName) {
    var postData = {};
    var terminalTypes = [];
    var customerKeywordDialog = $("#customerKeywordDialog");
    var checkbox = customerKeywordDialog.find(":checkbox[name='synchronousAddition']:checked");
    if (checkbox.length > 0) {
        terminalTypes = ['PC', 'Phone'];
    } else {
        var terminalType = $("#chargeForm").find("#terminalType").val();
        terminalTypes.push($.trim(terminalType));
    }
    var domain = customerKeywordDialog.find("#domain").val();
    var bearPawNumber = customerKeywordDialog.find("#bearPawNumber").val();
    if (bearPawNumber !== "") {
        postData.bearPawNumber = bearPawNumber;
    }
    var keywordStr = customerKeywordDialog.find("#customerKeywordDialogContent").val();
    if (keywordStr === "") {
        $.messager.alert('提示', '请输入关键字！！', 'warning');
        customerKeywordDialog.find("#customerKeywordDialogContent").focus();
        return false;
    }
    keywordStr = keywordStr.replace(/[，|\r\n]/g, ",").replace(/[\s+]/g, "");
    if (keywordStr.substring(keywordStr.length - 1) === ','){
        keywordStr = keywordStr.substring(0, keywordStr.length - 1);
    }
    var keywords = keywordStr.split(',');
    keywords = keywords.filter(function (keyword, index) {
        return keywords.indexOf(keyword) === index && keyword !== '';
    });
    var type = customerKeywordDialog.find("#qzSettingEntryType").val();
    var searchEngine = customerKeywordDialog.find("#searchEngine").val();
    var keywordEffect = customerKeywordDialog.find("#keywordEffect").val();
    var optimizeGroupName = customerKeywordDialog.find("#optimizeGroupName").val();
    if (optimizeGroupName === "") {
        optimizeGroupName = tempOptimizeGroupName;
    }
    postData.qzSettingUuid = qzSettingUuid;
    postData.customerUuid = customerUuid;
    postData.domain = $.trim(domain);
    postData.optimizeGroupName = optimizeGroupName;
    postData.type = type;
    postData.searchEngine = searchEngine;
    postData.terminalTypes = terminalTypes;
    postData.keywords = keywords;
    postData.keywordEffect = keywordEffect;
    $.ajax({
        url: '/internal/qzsetting/saveQZSettingCustomerKeywords',
        type: 'POST',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (data) {
            if (data) {
                $().toastmessage('showSuccessToast', "保存成功！");
                $("#customerKeywordDialog").dialog("close");
            } else {
                $().toastmessage('showErrorToast', "保存失败！");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "保存失败！");
        }
    });
}
function changePaging(currentPage, pageSize) {
    var chargeForm = $("#chargeForm");
    chargeForm.find("#resetPagingParam").val(false);
    chargeForm.find("#currentPageNumberHidden").val(currentPage);
    chargeForm.find("#pageSizeHidden").val(pageSize);
    chargeForm.submit();
}
function resetSearchCondition(days) {
    var chargeForm = $("#chargeForm");
    var customerInfo = chargeForm.find("#customerInfo").val();
    var customerUuid = customerInfo.substr(customerInfo.lastIndexOf("_") + 1);
    chargeForm.find("#customerUuid").val(customerUuid);
    chargeForm.find("#dateRangeType").val(days);

    var domain = chargeForm.find("#domain").val();
    var group = chargeForm.find("#group").val();
    if(domain != "") {
        chargeForm.find("#domain").val($.trim(domain));
    }
    if(group != "") {
        chargeForm.find("#group").val($.trim(group));
    }
    chargeForm.find("#currentPageNumberHidden").val(1);
    chargeForm.submit();
}
function qzSettingSelectAll(self){
    var a = document.getElementsByName("uuid");
    if(self.checked){
        for(var i = 0;i<a.length;i++){
            a[i].checked = true;
        }
    }else{
        for(var i = 0;i<a.length;i++){
            a[i].checked = false;
        }
    }
}
function delQZSetting(uuid) {
    parent.$.messager.confirm('确认', "确实要删除这个全站设置吗?", function (b) {
        if (b) {
            $.ajax({
                url: '/internal/qzsetting/delete/' + uuid,
                type: 'Get',
                success: function (data) {
                    if(data){
                        $().toastmessage('showSuccessToast', "删除成功！", true);
                    }else{
                        $().toastmessage('showErrorToast', "删除失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "删除失败");
                }
            });
        }
    });
}
function getSelectedIDs() {
    var uuids = '';
    $.each($("input[name=uuid]:checkbox:checked"), function(){
        if(uuids === ''){
            uuids = $(this).val();
        }else{
            uuids = uuids + "," + $(this).val();
        }
    });
    return uuids;
}
function getSelectedUsefulIDs() {
    var uuids = '';
    $.each($("input[name=uuid]:checkbox:checked"), function(){
        if ($(this).parent().parent().parent().find("div.rank-wrap1 div").length > 0) {
            if(uuids === ''){
                uuids = $(this).val();
            }else{
                uuids = uuids + "," + $(this).val();
            }
        }
    });
    return uuids;
}
function delSelectedQZSettings(self) {
    var uuids = getSelectedIDs();
    if(uuids === ''){
        $.messager.alert('提示', '请选择要操作的设置信息!!', 'info');
        return false;
    }
    parent.$.messager.confirm('确认', "确实要删除这些站点设置信息吗?", function (b) {
        if (b) {
            var postData = {};
            postData.uuids = uuids.split(",");
            $.ajax({
                url: '/internal/qzsetting/deleteQZSettings',
                data: JSON.stringify(postData),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    if(data){
                        $().toastmessage('showSuccessToast', "操作成功", true);
                    }else{
                        $().toastmessage('showErrorToast', "操作失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "操作失败");
                }
            });
        }
    });
}
function immediatelyUpdateQZSettings(type) {
    var uuids = getSelectedUsefulIDs();
    if(uuids === ''){
        $.messager.alert('提示', '请选择正确的要操作的站点信息, 必须包含至少一个是指定词的站点！！', 'info');
        return false;
    }
    var urlType = '';
    switch (type) {
        case "updateSettings":
            urlType = "updateImmediately";
            break;
        case "startMonitor":
            urlType = "startMonitorImmediately";
            break;
        case "updateQZKeywordEffect":
            urlType = "updateQZKeywordEffectImmediately";
            break;
        default:
            break;
    }
    updateImmediately(uuids, urlType)
}
function updateImmediately(uuids, urlType) {
    switch (urlType) {
        case "updateImmediately":
            if (!confirm("确实要马上更新这些站点设置吗?")) return false;
            break;
        case "startMonitorImmediately":
            if (!confirm("确实要启动这些站点所有终端的达标监控吗?")) return false;
            break;
        case "updateQZKeywordEffectImmediately":
            if (!confirm("确认修改这些站点所有终端下操作的关键词的作用为指定词吗?")) return false;
            break;
        default:
            break;
    }
    var postData = {};
    postData.uuids = uuids;
    $.ajax({
        url: '/internal/qzsetting/' + urlType,
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        success: function (data) {
            if(data){
                $().toastmessage('showSuccessToast', "操作成功", true);
            }else{
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}

function updateQZSettingStatus(status) {
    var uuids = getSelectedIDs();
    if(uuids === ''){
        $.messager.alert('提示', '请选择要操作的整站！！', 'info');
        return false;
    }
    if(status === 1) {
        if (!confirm("确认要激活选中的整站吗?")) return false;
    } else if (status === 0) {
        if (!confirm("确认要暂停选中的整站吗?")) return false;
    }
    var postData = {};
    postData.uuids = uuids.split(",");
    postData.status = status;
    $.ajax({
        url: '/internal/qzsetting/updateQZSettingStatus',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if(data){
                $().toastmessage('showSuccessToast', "操作成功", true);
            }else{
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}
function updateQZSettingRenewalStatus(renewalStatus) {
    var uuids = getSelectedIDs();
    if(uuids === ''){
        $.messager.alert('提示', '请选择要操作的整站！！', 'info');
        return false;
    }
    if(renewalStatus === 1) {
        if (!confirm("确认选中的整站要续费吗?")) return false;
    } else if (renewalStatus === 0) {
        if (!confirm("确认要停止收取所选整站的费用吗?")) return false;
    }
    var postData = {};
    postData.uuids = uuids.split(",");
    postData.renewalStatus = renewalStatus;
    $.ajax({
        url: '/internal/qzsetting/updateQZSettingRenewalStatus',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout: 5000,
        type: 'POST',
        success: function (data) {
            if(data){
                $().toastmessage('showSuccessToast', "操作成功", true);
            }else{
                $().toastmessage('showErrorToast', "操作失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "操作失败");
        }
    });
}
function toTimeFormat(time) {
    var date = toDateFormat(time);
    var hours = time.getHours() < 10 ? ("0" + time.getHours()) : time.getHours();
    var minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
    var seconds = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
    return date + " " + hours + ":" + minutes + ":" + seconds;
}
function toDateFormat (time) {
    return time.getFullYear() + "-" +
        (time.getMonth() + 1) + "-" +
        time.getDate();
}
function saveChargeLog(self) {
    var chargeDialog = $("#chargeDialog");
    var selectedOperationTypes = chargeDialog.find("input[name=operationType]:checkbox:checked");
    var saveChargeLogFlag = true;
    if (selectedOperationTypes.length === 0) {
        $.messager.alert('提示', '必须选择一个收费项才能收费！！', 'warning');
        saveChargeLogFlag = false;
        return false;
    }
    var chargeLogs = [];
    $.each(selectedOperationTypes, function (index, val) {
        var chargeLog = {};
        chargeLog.qzOperationTypeUuid = chargeDialog.find(
            "#qzOperationTypeUuid" + val.id).val();
        chargeLog.planChargeDate = chargeDialog.find("#planChargeDate" + val.id).val();
        chargeLog.actualChargeDate = chargeDialog.find("#actualChargeDate" + val.id).val();
        chargeLog.receivableAmount = chargeDialog.find("#receivableAmount" + val.id).val();
        chargeLog.actualAmount = chargeDialog.find("#actualAmount" + val.id).val();
        chargeLog.nextChargeDate = chargeDialog.find("#nextChargeDate" + val.id).val();
        if (chargeLog.nextChargeDate === "" || chargeLog.nextChargeDate == null) {
            $.messager.alert('提示', '下次收费日期为必填！！', 'warning');
            chargeDialog.find("#nextChargeDate" + val.id).focus();
            saveChargeLogFlag = false;
            return false;
        }
        if (chargeLog.actualAmount === "" || chargeLog.actualAmount == null) {
            $.messager.alert('提示', '实收金额为必填！！', 'warning');
            chargeDialog.find("#actualAmount" + val.id).focus();
            saveChargeLogFlag = false;
            return false;
        }
        if (!reg.test(chargeLog.actualAmount)) {
            $.messager.alert('提示', '请输入合理的金额！！', 'warning');
            chargeDialog.find("#actualAmount" + val.id).focus();
            saveChargeLogFlag = false;
            return false;
        }
        if (chargeLog.actualChargeDate === "" || chargeLog.actualChargeDate == null) {
            $.messager.alert('提示', '实际收费日期为必填！！', 'warning');
            chargeDialog.find("#actualChargeDate" + val.id).focus();
            saveChargeLogFlag = false;
            return false;
        }
        chargeLogs.push(chargeLog);
    });
    if(saveChargeLogFlag) {
        parent.$.messager.confirm('确认', "确认收费?", function (b) {
            if (b) {
                $.ajax({
                    url: '/internal/qzchargelog/save',
                    data: JSON.stringify(chargeLogs),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (data) {
                        resetChargeDialog();
                        if (data != null && data !== "") {
                            $().toastmessage('showSuccessToast', "收费成功！", true);
                            $("#chargeDialog").dialog("close");
                        } else {
                            $().toastmessage('showErrorToast', "收费失败");
                        }
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "收费失败");
                    }
                });
            } else {
                $().toastmessage('showErrorToast', '取消收费！！！');
            }
        });
    }
}
function calTotalAmount() {
    var totalAmount = 0;
    var chargeDialog = $("#chargeDialog");
    $.each(chargeDialog.find("input[name=operationType]:checkbox:checked"), function(idx, val){
        totalAmount = totalAmount + Number(chargeDialog.find("#actualAmount" + val.id).val());
    });
    var str = new String(totalAmount);
    var total = str.replace( /\B(?=(?:\d{3})+$)/g, ',' );
    chargeDialog.find("#totalAmount").html(total + "元");
}
function showChargeLog(uuid, self) {
    $("#chargeLogListTable  tr:not(:first)").remove();
    $.ajax({
        url: '/internal/qzchargelog/chargesList/' + uuid,
        type: 'Get',
        success: function (qzChargeLogs) {
            if (qzChargeLogs != null && qzChargeLogs.length > 0) {
                $.each(qzChargeLogs, function (idx, val) {
                    var newTr = document.createElement("tr")
                    var chargeLogElements = [
                        toDateFormat(new Date(val.actualChargeDate)),
                        val.operationType,
                        val.actualAmount,
                        val.userName,
                        toTimeFormat(new Date(val.createTime))
                    ];
                    $.each(chargeLogElements, function () {
                        var newTd = document.createElement("td");
                        newTr.appendChild(newTd);
                        newTd.innerHTML = this;
                    });
                    $("#chargeLogListTable")[0].lastChild.appendChild(newTr);
                });
                $("#chargeLogListDiv").show();
                $("#chargeLogListDiv").dialog({
                    resizable: false,
                    width: 370,
                    title:"收费记录",
                    modal: true,
                    buttons: [{
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#chargeLogListDiv").dialog("close");
                        }
                    }]
                });
                $("#chargeLogListDiv").dialog("open");
                $("#chargeLogListDiv").window("resize",{top:$(document).scrollTop() + 100});
            } else {
                $().toastmessage('showErrorToast', '暂无收费记录！！！');
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败！");
        }
    });
}
function showChargeDialog(uuid,contactPerson,domain,self) {
    var chargeDialogObj = $("#chargeDialog");
    chargeDialogObj.find("#qzSettingCustomer").val(contactPerson);
    chargeDialogObj.find("#qzSettingDomain").val(domain);
    $.ajax({
        url: '/internal/qzchargelog/getQZChargeLog/' + uuid,
        type: 'Get',
        success: function (chargeInfos) {
            if(chargeInfos != null && chargeInfos.length > 0){
                resetChargeDialog();
                var totalAmount = 0;
                var showFlag = false;
                $.each(chargeInfos, function(idx, val){
                    var checkbox = chargeDialogObj.find("#" + val.operationType);
                    if(val.qzOperationTypeUuid != null) {
                        // 存在此类操作类型
                        chargeDialogObj.find("#qzOperationTypeUuid" + val.operationType).val(val.qzOperationTypeUuid);
                        chargeDialogObj.find("#initialKeywordCount" + val.operationType).val(val.initialKeywordCount);
                        chargeDialogObj.find("#currentKeywordCount" + val.operationType).val(val.currentKeywordCount);
                        chargeDialogObj.find("#receivableAmount" + val.operationType).val(val.receivableAmount);
                        chargeDialogObj.find("#actualAmount" + val.operationType).val(val.receivableAmount);
                        totalAmount = totalAmount + Number(val.receivableAmount == null ? 0 : val.receivableAmount);
                        chargeDialogObj.find("#checkCharge" + val.operationType).css("display","block");
                        // 达标
                        if(val.planChargeDate != null) {
                            checkbox[0].checked = true;
                            chargeDialogObj.find("#" + val.operationType + "ChargeInfo").css("display","block");
                            var date = new Date(val.planChargeDate);
                            var year = date.getFullYear();
                            var month = date.getMonth() + 2;
                            if(month < 10) {
                                month = "0" + month;
                            } else {
                                if(month > 12) {
                                    month = "01";
                                    year = year + 1;
                                }
                            }
                            var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
                            var nextChargeDate = year + "-" + month + "-" + day;
                            chargeDialogObj.find("#receivableAmount" + val.operationType).val(val.receivableAmount);
                            chargeDialogObj.find("#planChargeDate" + val.operationType).val(val.planChargeDate);
                            chargeDialogObj.find("#nextChargeDate" + val.operationType).val(nextChargeDate);
                            showFlag = true;
                        } else {
                            checkbox[0].checked = false;
                            chargeDialogObj.find("#" + val.operationType + "ChargeInfo").css("display","none");
                        }
                        chargeDialogObj.find("#actualChargeDate" + val.operationType).val(today);
                    } else {
                        chargeDialogObj.find("#checkCharge" + val.operationType).css("display","none");
                        chargeDialogObj.find("#" + val.operationType + "ChargeInfo").css("display","none");
                    }
                });

                if(!showFlag){
                    chargeDialogObj.find("#chargeInfoTable").css("height",0);
                }

                var s = new String(totalAmount);
                var total = s.replace(/\B(?=(?:\d{3})+$)/g, ',');
                chargeDialogObj.find("#totalAmount").html(total+"元");
                $("#chargeDialog").show();
                $("#chargeDialog").dialog({
                    resizable: false,
                    modal: true,
                    width: 390,
                    height:360,
                    title: '收费',
                    buttons: [{
                        text: '收费',
                        iconCls: 'icon-ok',
                        handler: function () {
                            saveChargeLog(this);
                        }
                    }, {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#chargeDialog").dialog("close");
                        }
                    }]
                });
                $("#chargeDialog").dialog("open");
                $("#chargeDialog").window("resize",{top:$(document).scrollTop() + 100});
            }else{
                $().toastmessage('showErrorToast', "获取信息失败！");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败！");
        }
    });
}
function resetChargeDialog() {
    var chargeDialogObj = $("#chargeDialog");
    chargeDialogObj.find("#PCChargeInfo").css("display","none");
    chargeDialogObj.find("#PhoneChargeInfo").css("display","none");
    chargeDialogObj.find("#checkChargePC").css("display","none");
    chargeDialogObj.find("#checkChargePhone").css("display","none");
    var operationTypes = chargeDialogObj.find("input[name=operationType]");
    $.each(operationTypes,function (idx, val) {
        val.checked = false;
        chargeDialogObj.find("#qzOperationTypeUuid" + val.id).val("");
        chargeDialogObj.find("#initialKeywordCount" + val.id).val("");
        chargeDialogObj.find("#currentKeywordCount" + val.id).val("");
        chargeDialogObj.find("#receivableAmount" + val.id).val("");
        chargeDialogObj.find("#planChargeDate" + val.id).val("");
        chargeDialogObj.find("#nextChargeDate" + val.id).val("");
        chargeDialogObj.find("#actualAmount" + val.id).val("");
        chargeDialogObj.find("#actualChargeDate" + val.id).val("");
    });
}
function dealChargeTable(operationType) {
    var chargeDialog = $("#chargeDialog");
    var checkboxObj = chargeDialog.find("#" + operationType);
    var chargeInfoObj = chargeDialog.find("#" + operationType + "ChargeInfo");
    if (chargeInfoObj.css("display") == "none" || checkboxObj[0].checked == true) {
        chargeInfoObj.css("display", "block");
    } else {
        chargeInfoObj.css("display", "none");
    }
    calTotalAmount();
    var selectedOperationTypes = chargeDialog.find("input[name=operationType]:checkbox:checked");
    if (selectedOperationTypes.length == 0) {
        chargeDialog.find("#chargeInfoTable").css("height",0);
    }
}
function createSettingDialog() {
    $("#changeSettingDialog").show();
    $("#changeSettingDialog").dialog({
        resizable: false,
        height: 598,
        width: 700,
        title: '全站设置',
        modal: true,
        buttons: [{
            text: '保存不刷新',
            iconCls: 'icon-ok',
            handler: function () {
                saveChangeSetting(this, false);
            }
        }, {
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveChangeSetting(this, true);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#changeSettingForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#changeSettingDialog").dialog("close");
                    $('#changeSettingForm')[0].reset();
                }
            }],
        onClose: function () {
            $('#changeSettingForm')[0].reset();
            $("#changeSettingDialog").find("#PC").attr("status", 1);
            $("#changeSettingDialog").find("#Phone").attr("status", 1);
            $("#changeSettingDialog").find("#optimizationType").find("div").each(function() {
                $(this).css("display", "none");
            });
            $("#changeSettingDialog").find("input:checkbox:checked").each(function() {
               $(this).prop("checked", false);
            });
        }
    });
    $("#changeSettingDialog").dialog("open");
    $("#changeSettingDialog").window("resize",
        {
            top:$(document).scrollTop() + 150,
            left: $(document).scrollLeft() + $(window).width() / 2 - 340
        }
    );
}
function resetSettingDialog() {
    var settingDialogDiv = $("#changeSettingDialog");
    settingDialogDiv.find("#qzSettingUuid").val("");
    settingDialogDiv.find("#qzSettingCustomer").val("");
    settingDialogDiv.find("#qzSettingDomain").val("");
    settingDialogDiv.find("#bearPawNumber").val("");
    settingDialogDiv.find("#qzCategoryTagNames").val("");
    settingDialogDiv.find("#groupMaxCustomerKeywordCount").val("5000");
    if ($(".datalist-list #isBaiduEngine").val() === 'true') {
        settingDialogDiv.find("#qzSettingAutoCrawlKeywordFlag").val("0");
        settingDialogDiv.find("#qzSettingIgnoreNoIndex").val("1");
        settingDialogDiv.find("#qzSettingIgnoreNoOrder").val("1");
        settingDialogDiv.find("#qzSettingInterval").val("2");
    }
    settingDialogDiv.find("#qzSettingStartMonitor").val("0");
    settingDialogDiv.find("#qzSettingJoinReady").val("0");
    clearInfo("Both");
}
function clearInfo(type) {
    var settingDialogObj = $("#changeSettingDialog");
    if(type === "Both") {
        clearInfo("PC");
        clearInfo("Phone");
        var standardSpecies = 'aiZhan';
        if ($(".datalist-list #isBaiduEngine").val() === 'false') {
            standardSpecies = 'designationWord';
        }
        clearStandardInfo("PC", standardSpecies, 1);
        clearStandardInfo("Phone", standardSpecies, 1);
    } else {
        // 清空分组表格信息
        settingDialogObj.find("#group" + type).val("");
        settingDialogObj.find("#currentKeywordCount" + type).val("");
        settingDialogObj.find("#monitorRemark" + type).val("");
        settingDialogObj.find("#qzOperationTypeUuid" + type).val("");
        settingDialogObj.find("#" + type)[0].checked = false;
        settingDialogObj.find("#operationTypeSummaryInfo" + type).css("display","none");
    }
}
function clearStandardInfo(type, standardSpecies, hideStatus) {
    var settingDialogObj = $("#changeSettingDialog");
    standardSpecies = changeStandardSpecies(standardSpecies);
    // 修改表头信息
    settingDialogObj.find("#chargeRule" + type).find("thead tr td").text(standardSpecies + "收费规则");
    // 清空规则表格信息
    settingDialogObj.find("#chargeRule" + type).find("tbody tr:not(:first,:last)").remove();
    if (hideStatus) {
        settingDialogObj.find("#chargeRule" + type).css("display","none");
    }
}
function changeStandardSpecies(standardSpecies){
    switch (standardSpecies) {
        case 'aiZhan':
            standardSpecies = '爱站';
            break;
        case '5118':
            break;
        case 'designationWord':
            standardSpecies = '指定词';
            break;
        case 'other':
            standardSpecies = '其他';
            break;
        default:
            break;
    }
    return standardSpecies;
}
function showSettingDialog(self) {
    resetSettingDialog();
    var uuid = $(self).parent().parent().find("input:checkbox[name='uuid']").val();
    if(uuid === undefined){
        createSettingDialog();
        return;
    }
    $.ajax({
        url: '/internal/qzsetting/getQZSetting/' + uuid,
        type: 'Get',
        success: function (qzSetting) {
            if(qzSetting != null){
                initSettingDialog(qzSetting, self);
                createSettingDialog();
            }else{
                $().toastmessage('showErrorToast', "获取信息失败！");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败！");
        }
    });
}
function initSettingDialog(qzSetting, self) {
    var PCType = false;
    var PhoneType = false;
    var PCOptimizationType = false;
    var PhoneOptimizationType = false;
    var settingDialogDiv = $("#changeSettingDialog");
    settingDialogDiv.find("#qzSettingUuid").val(qzSetting.uuid);
    settingDialogDiv.find("#groupMaxCustomerKeywordCount").val(qzSetting.groupMaxCustomerKeywordCount);
    settingDialogDiv.find("#bearPawNumber").val(qzSetting.bearPawNumber);
    settingDialogDiv.find("#qzSettingCustomer").val(qzSetting.contactPerson + "_____" + qzSetting.customerUuid);
    settingDialogDiv.find("#qzSettingDomain").val(qzSetting.domain != null ? qzSetting.domain : "");
    settingDialogDiv.find("#searchEngine").val(qzSetting.searchEngine != null ? qzSetting.searchEngine : "");
    var isBaiduEngine = $(".datalist-list #isBaiduEngine").val();
    if (isBaiduEngine === 'true') {
        settingDialogDiv.find("#qzSettingAutoCrawlKeywordFlag").val(qzSetting.autoCrawlKeywordFlag ? "1" : "0");
        settingDialogDiv.find("#qzSettingIgnoreNoIndex").val(qzSetting.ignoreNoIndex ? "1" : "0");
        settingDialogDiv.find("#qzSettingIgnoreNoOrder").val(qzSetting.ignoreNoOrder ? "1" : "0");
        settingDialogDiv.find("#qzSettingInterval").val(qzSetting.updateInterval != null ? qzSetting.updateInterval : "");
    }
    settingDialogDiv.find("#qzSettingStartMonitor").val(qzSetting.fIsMonitor ? "1" : "0");
    settingDialogDiv.find("#qzSettingJoinReady").val(qzSetting.fIsReady ? "1" : "0");
    settingDialogDiv.find("#qzSettingEntryType").val(qzSetting.type != null ? qzSetting.type : "");
    var organizationName = $(self).parent().parent().find("span.organization-name a").text();
    var flag = true;
    if (organizationName === '优化部') {
        flag = false;
    }

    // 操作类型表填充数据
    $.each(qzSetting.qzOperationTypes, function (idx, val) {
        if (isBaiduEngine === 'false') {
            settingDialogDiv.find("#optimizationType" + val.operationType).find("input")[0].checked = true;
        } else {
            settingDialogDiv.find("#optimizationType" + val.operationType).find("input[value='"+ val.optimizationType +"']")[0].checked = true;
        }
        settingDialogDiv.find("#optimizationType" + val.operationType).css("display", "block");
        settingDialogDiv.find("#group" + val.operationType).val(val.group);
        settingDialogDiv.find("#subDomainName" + val.operationType).val(val.subDomainName);
        settingDialogDiv.find("#monitorRemark" + val.operationType).val(val.monitorRemark === null ? '' : val.monitorRemark);
        settingDialogDiv.find("#currentKeywordCount" + val.operationType).val(val.currentKeywordCount);
        /* 限制最大词数 */
        settingDialogDiv.find("#maxKeywordCount" + val.operationType).val(val.maxKeywordCount);
        settingDialogDiv.find("#qzSettingUuid" + val.operationType).val(val.uuid);

        var isSEO = $(".datalist-list #isSEO").val();
        // 构造规则表
        if (val.qzChargeRules !== null) {
            $.each(val.qzChargeRules, function (chargeRuleIdx, chargeRuleVal) {
                if (isSEO !== "true") {
                    if (isBaiduEngine === 'true') {
                        settingDialogDiv.find('#aiZhan' + val.operationType + 'StandardSpecies')[0].checked = false;
                    }
                    settingDialogDiv.find("#" + chargeRuleVal.standardSpecies + val.operationType + "StandardSpecies")[0].checked = true;
                    settingDialogDiv.find("#chargeRule" + val.operationType).css("display", "block");
                    settingDialogDiv.find("#chargeRule" + val.operationType).find("thead tr td").text(changeStandardSpecies(chargeRuleVal.standardSpecies) + "收费规则");
                    addRow("chargeRule" + val.operationType, chargeRuleVal);
                }
            });
        }
        if (val.operationType === 'PC') {
            PCType = true;
            if (val.optimizationType === 0) {
                PCOptimizationType = true;
            }
        }
        if (val.operationType === 'Phone') {
            PhoneType = true;
            if (val.optimizationType === 0) {
                PhoneOptimizationType = true;
            }
        }
    });
    // 分类标签
    var tagNames = "";
    $.each(qzSetting.qzCategoryTags, function (idx, val) {
        tagNames += val.tagName + ",";
    });
    settingDialogDiv.find("#qzCategoryTagNames").val(tagNames.substring(0,tagNames.length-1));
    if (PCType) {
        settingDialogDiv.find("#PC")[0].checked = true;
        settingDialogDiv.find("#operationTypeSummaryInfoPC").css("display", "block");
        if (!flag || PCOptimizationType) {
            settingDialogDiv.find("#standardSpeciesPC label").css("display", "none");
            settingDialogDiv.find("#standardSpeciesPC input").css("display", "none");
            settingDialogDiv.find("#chargeRulePC").css("display", "none");
        }
    }
    if (PhoneType) {
        settingDialogDiv.find("#Phone")[0].checked = true;
        settingDialogDiv.find("#operationTypeSummaryInfoPhone").css("display", "block");
        if (!flag || PhoneOptimizationType) {
            settingDialogDiv.find("#standardSpeciesPhone label").css("display", "none");
            settingDialogDiv.find("#standardSpeciesPhone input").css("display", "none");
            settingDialogDiv.find("#chargeRulePhone").css("display", "none");
        }
    }
    if (!flag) {
        settingDialogDiv.find("#PC").attr("status", 0);
        settingDialogDiv.find("#Phone").attr("status", 0);
    }
}
//规则表验证
var reg = /^[1-9]\d*$/;
function saveChangeSetting(self, refresh) {
    var settingDialogDiv = $("#changeSettingDialog");
    var qzSetting = {};
    qzSetting.uuid = settingDialogDiv.find("#qzSettingUuid").val().trim();

    var customer = settingDialogDiv.find("#qzSettingCustomer").val().trim();
    if (customer == null || customer === "") {
        $.messager.alert('提示', '请选择客户!!', 'warning');
        settingDialogDiv.find("#qzSettingCustomer").focus();
        return false;
    }
    qzSetting.domain = settingDialogDiv.find("#qzSettingDomain").val().trim();
    if (qzSetting.domain == null || qzSetting.domain === "") {
        $.messager.alert('提示', '请输入域名!!', 'warning');
        settingDialogDiv.find("#qzSettingDomain").focus();
        return false;
    }
    qzSetting.bearPawNumber = settingDialogDiv.find("#bearPawNumber").val().trim();
    qzSetting.searchEngine = settingDialogDiv.find("#searchEngine").val();
    if (settingDialogDiv.find("#qzSettingAutoCrawlKeywordFlag").length > 0) {
        qzSetting.autoCrawlKeywordFlag = settingDialogDiv.find("#qzSettingAutoCrawlKeywordFlag").val() === "1" ? true : false;
        qzSetting.ignoreNoIndex = settingDialogDiv.find("#qzSettingIgnoreNoIndex").val() === "1" ? true : false;
        qzSetting.ignoreNoOrder = settingDialogDiv.find("#qzSettingIgnoreNoOrder").val() === "1" ? true : false;
        qzSetting.updateInterval = settingDialogDiv.find("#qzSettingInterval").val();
        qzSetting.groupMaxCustomerKeywordCount = settingDialogDiv.find("#groupMaxCustomerKeywordCount").val();
    } else {
        qzSetting.autoCrawlKeywordFlag = false;
        qzSetting.ignoreNoIndex = true;
        qzSetting.ignoreNoOrder = true;
        qzSetting.updateInterval = 2;
        qzSetting.groupMaxCustomerKeywordCount = 5000;
    }
    if (settingDialogDiv.find("#qzSettingStartMonitor").length > 0) {
        qzSetting.fIsMonitor = settingDialogDiv.find("#qzSettingStartMonitor").val() === "1" ? true : false;
    } else {
        qzSetting.fIsMonitor = false;
    }
    if (settingDialogDiv.find("#qzSettingJoinReady").length > 0) {
        qzSetting.fIsReady = settingDialogDiv.find("#qzSettingJoinReady").val() === "1" ? true : false;
    } else {
        qzSetting.fIsReady = false;
    }
    qzSetting.pcGroup = settingDialogDiv.find("#groupPC").val().trim();
    qzSetting.phoneGroup = settingDialogDiv.find("#groupPhone").val().trim();
    if(qzSetting.pcGroup === "") {
        qzSetting.pcGroup = null;
    }
    if(qzSetting.phoneGroup === "") {
        qzSetting.phoneGroup = null;
    }

    if (customer != null && customer !== '') {
        var customerArray = customer.split("_____");
        if (customerArray.length === 2) {
            qzSetting.customerUuid = customerArray[1];
        } else {
            $.messager.alert('提示', '请从列表中选择客户！！！', 'info');
            settingDialogDiv.find("#qzSettingCustomer").focus();
            return false;
        }
    }
    qzSetting.type = settingDialogDiv.find("#qzSettingEntryType").val();
    qzSetting.qzOperationTypes = [];//操作类型表
    qzSetting.qzOperationTypes.qzChargeRules = [];//收费规则
    qzSetting.qzCategoryTags = []; //分类标签表

    var tagNames = settingDialogDiv.find("#qzCategoryTagNames").val().replace(/(，)+/g, ",");
    if (tagNames !== "") {
        var tagNameArr = tagNames.split(",");
        tagNameArr = unique(tagNameArr);
        $.each(tagNameArr, function (idx, val) {
            var qzCategoryTag = {};
            qzCategoryTag.tagName = $.trim(val);
            qzSetting.qzCategoryTags.push(qzCategoryTag);
        });
    }
    var checkedObjs = settingDialogDiv.find("input[name=operationType]:checkbox:checked");
    var validationFlag = true;
    $.each(checkedObjs, function (idx, val) {
        var operationType = {};
        operationType.qzChargeRules = [];
        operationType.operationType = val.id;
        var optimizationType = settingDialogDiv.find("#optimizationType" + val.id).find("input:checkbox:checked").val();
        if (optimizationType === undefined) {
            $.messager.alert('提示', '请选中所选终端下的达标优化类型！！！', 'info');
            validationFlag = false;
            return false;
        }
        operationType.optimizationType = optimizationType;
        operationType.group = settingDialogDiv.find("#group" + val.id).val().trim();
        operationType.currentKeywordCount = settingDialogDiv.find("#currentKeywordCount" + val.id).val().trim();
        var maxKeywordCount = settingDialogDiv.find("#maxKeywordCount" + val.id).val();
        if (maxKeywordCount !== undefined) {
            operationType.maxKeywordCount = maxKeywordCount.trim();
        } else {
            operationType.maxKeywordCount = 1000;
        }
        operationType.subDomainName = settingDialogDiv.find("#subDomainName" + val.id).val().trim();
        if (operationType.group == null || operationType.group === "") {
            $.messager.alert('提示', '请输入分组！！', 'warning');
            settingDialogDiv.find("#group" + val.id).focus();
            validationFlag = false;
            return false;
        }else if(operationType.group.length>20){
            $.messager.alert('提示', operationType.operationType + '分组大于20位, 请重新输入！！', 'warning');
            settingDialogDiv.find("#group" + val.id).focus();
            validationFlag = false;
            return false;
        }

        if (qzSetting.autoCrawlKeywordFlag && maxKeywordCount !== undefined && (operationType.maxKeywordCount === "" || !reg.test(operationType.maxKeywordCount))){
            $.messager.alert('提示', '请输入限制词量！！', 'warning');
            settingDialogDiv.find("#maxKeywordCount" + val.id).focus();
            validationFlag = false;
            return false;
        }
        operationType.standardType = "satisfyAll";

        var isSEO = $(".datalist-list #isSEO").val();
        if (isSEO === "true") {
            var chargeRule = {};
            chargeRule.standardSpecies = 'aiZhan';
            chargeRule.startKeywordCount = 1;
            chargeRule.endKeywordCount = 2;
            chargeRule.amount = 3;
            operationType.qzChargeRules.push(chargeRule);
        } else {
            operationType.monitorRemark = settingDialogDiv.find("#monitorRemark" + val.id).val().trim();
            if (operationType.optimizationType !== '0') {
                var standardSpeciesObjs = $("#operationTypeSummaryInfo"+val.id).find("input[name='standardSpecies']:checkbox:checked");
                $.each(standardSpeciesObjs, function (i, v) {
                    var endKeyWordCountValue = -1;
                    //多条规则
                    var ruleObj = settingDialogDiv.find("#chargeRule" + val.id);
                    var trObjs = ruleObj.find("tbody tr:not(:first,:last)");
                    $.each(trObjs, function (idx, val) {
                        var startKeywordCountObj = $(val).find("input[name='startKeywordCount']");
                        var endKeywordCountObj = $(val).find("input[name='endKeywordCount']");
                        var amountObj = $(val).find("input[name='amount']");

                        var chargeRule = {};
                        chargeRule.standardSpecies = $(v).val();
                        chargeRule.startKeywordCount = startKeywordCountObj.val().trim();
                        chargeRule.endKeywordCount = endKeywordCountObj.val().trim();
                        chargeRule.amount = amountObj.val().trim();
                        operationType.qzChargeRules.push(chargeRule);

                        if (startKeywordCountObj.val() === null || startKeywordCountObj.val().trim() === "") {
                            $.messager.alert('提示', '请输入起始达标词数！！', 'warning');
                            startKeywordCountObj[0].focus();
                            validationFlag = false;
                            return false;
                        }
                        if (!reg.test(startKeywordCountObj.val().trim())) {
                            $.messager.alert('提示', '请输入数字！！', 'warning');
                            startKeywordCountObj.focus();
                            validationFlag = false;
                            return false;
                        }
                        var skc = Number(startKeywordCountObj.val().trim());
                        if (skc <= endKeyWordCountValue) {
                            $.messager.alert('提示', '起始达标词数过小！！', 'warning');
                            startKeywordCountObj.focus();
                            validationFlag = false;
                            return false;
                        }
                        if (idx < (trObjs.length - 1)) {
                            if (endKeywordCountObj.val() === null || endKeywordCountObj.val().trim() === "") {
                                $.messager.alert('提示', '请输入终止达标词数！！', 'warning');
                                endKeywordCountObj.focus();
                                validationFlag = false;
                                return false;
                            }
                        } else {
                            if(endKeywordCountObj.val() !== "" && operationType.currentKeywordCount>Number(endKeywordCountObj.val())){
                                $.messager.alert('提示', '最后一条规则中的终止达标词量必须大于当前词量！！', 'warning');
                                endKeywordCountObj.focus();
                                validationFlag = false;
                                return false;
                            }
                        }
                        if (endKeywordCountObj.val() !== "") {
                            if (!reg.test(endKeywordCountObj.val())) {
                                $.messager.alert('提示', '请输入数字！！', 'warning');
                                endKeywordCountObj.focus();
                                validationFlag = false;
                                return false;
                            }
                            if (Number(endKeywordCountObj.val().trim()) <= skc) {
                                $.messager.alert('提示', '终止达标词数必须大于起始达标词数！！', 'warning');
                                endKeywordCountObj.focus();
                                validationFlag = false;
                                return false;
                            }
                        }
                        if (amountObj.val() === null || amountObj.val().trim() === "") {
                            $.messager.alert('提示', '请输入价格！！', 'warning');
                            amountObj.focus();
                            validationFlag = false;
                            return false;
                        }
                        if (!reg.test(amountObj.val().trim())) {
                            $.messager.alert('提示', '输入的价格不合理！！', 'warning');
                            amountObj.focus();
                            validationFlag = false;
                            return false;
                        }
                        endKeyWordCountValue = Number(endKeywordCountObj.val().trim());
                    });
                });
            }
        }
        if (!validationFlag) {
            return false;
        }
        qzSetting.qzOperationTypes.push(operationType);
    });

    if (validationFlag) {
        if (checkedObjs.length === 0) {
            $.messager.alert('提示', '保存失败, 必须要增加一条规则！！', 'warning');
            return false;
        }
        hideSaveButton(true);
        $.ajax({
            url: '/internal/qzsetting/save',
            data: JSON.stringify(qzSetting),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'POST',
            success: function (data) {
                if (data != null && data !== "") {
                    $().toastmessage('showSuccessToast', "更新成功", refresh);
                } else {
                    hideSaveButton(false);
                    $().toastmessage('showErrorToast', "更新失败！");
                }
                $("#changeSettingDialog").dialog("close");
            },
            error: function () {
                hideSaveButton(false);
                $().toastmessage('showErrorToast', "更新失败！");
            }
        });
    }
}
function hideSaveButton(hideStatus) {
    var a_first_child = $("#changeSettingDialog").parent().find("div.dialog-button a:first-child");
    var a_second_child = $("#changeSettingDialog").parent().find("div.dialog-button a:nth-child(2)");
    if (hideStatus) {
        a_first_child.removeAttr('href');
        a_first_child.removeAttr('onclick');
        a_first_child.css("opacity", "0.5");
        a_second_child.removeAttr('href');
        a_second_child.removeAttr('onclick');
        a_second_child.css("opacity", "0.5");
    } else {
        a_first_child.attr("href", "javascript:void(0);");
        a_first_child.attr("onclick", "saveChangeSetting();");
        a_first_child.removeAttr("opacity");
        a_second_child.attr("href", "javascript:void(0);");
        a_second_child.attr("onclick", "saveChangeSetting();");
        a_second_child.removeAttr("opacity");
    }
}
function addRow(tableID){
    addRow(tableID, null);
}
function addRow(tableID, chargeRule){
    var tableObj = $("#" + tableID + " tbody");
    if (tableObj.length > 0) {
        var rowCount = tableObj.find("tr").length;
        var newRow = tableObj[0].insertRow(rowCount - 1); //插入新行

        var col1 = newRow.insertCell(0);
        col1.innerHTML="<input type='text' name='sequenceID' value='"+(rowCount - 1)+"' style='width:52px'/>";
        var col2 = newRow.insertCell(1);
        col2.innerHTML = "<input type='text' name='startKeywordCount' value='"+(chargeRule != null ? chargeRule.startKeywordCount : '')+"' style='width:76px'/>";
        var col3 = newRow.insertCell(2);
        col3.innerHTML = "<input type='text' name='endKeywordCount' value='"+((chargeRule != null && chargeRule.endKeywordCount != null) ? chargeRule.endKeywordCount : '')+"'  style='width:76px'/>";
        var col4 = newRow.insertCell(3);
        col4.innerHTML = "<input type='text' name='amount' value='"+(chargeRule != null ? chargeRule.amount : '')+"'  style='width:66px'/>";
        var col5 = newRow.insertCell(4);
        col5.innerHTML = "<input style='width:46px' type='button' value='删除' onclick='deleteCurrentRow(this.parentNode.parentNode)' />";

        $("#changeSettingDialog").css("height", $("#changeSettingDialog").height() + 25);
    }
}
function deleteCurrentRow(currentRow) {
    var index = currentRow.rowIndex;
    var tableObj = currentRow.parentNode.parentNode;
    if(tableObj.rows.length > 4) {
        tableObj.deleteRow(index);
        $.each($("#"+tableObj.id).find("input[name=sequenceID]"), function(idx, val){
            $(val).val(idx + 1);
        });
        if ($("#changeSettingDialog").height() > "412") {
            $("#changeSettingDialog").css("height", $("#changeSettingDialog").height() + 2);
        }
    } else {
        $().toastmessage('showErrorToast', '删除失败, 规则表不允许为空！！')
    }
}

function dealSettingTable(self, operationType, type) {
    var settingDialogDiv = $("#changeSettingDialog");
    // 控制唯一选中和不重复显示
    var display = true;

    if ($(self).parent().find("input:checkbox:checked").length > 1) {
        if ($(self).parent().find("input[value='0']:checkbox:checked").length > 0) {
            display = true;
        } else {
            display = false;
        }
    }

    var optimizationType = $(self).val();
    if ($(self)[0].checked) {
        $(self).parent().find("input:checkbox:checked").each(function () {
            if ($(this).val() !== optimizationType) {
                $(this).prop("checked", false);
            }
        });
    } else {
        if (type === 1) {
            $(self)[0].checked = true;
            $().toastmessage('showErrorToast', '必须选中一种达标优化类型');
            return false;
        }
    }

    if (display) {
        var groupObj = settingDialogDiv.find('#operationTypeSummaryInfo' + operationType);
        var checkboxObj = settingDialogDiv.find('#' + operationType);
        var isSEO = $(".datalist-list #isSEO").val();
        var status = $(checkboxObj).attr("status");
        var standardSpecies = 'aiZhan';
        if ($(".datalist-list #isBaiduEngine").val() === 'false') {
            standardSpecies = 'designationWord';
        }
        if (checkboxObj[0].checked === true) {
            groupObj.css("display","block");
            if (isSEO === "false" && optimizationType !== '0') {
                $("#chargeRule" + operationType).css("display", "block");
                if ($("#standardSpecies" + operationType).find("input:checkbox:checked").length > 0) {
                    $("#" + standardSpecies + operationType +"StandardSpecies")[0].checked = false;
                } else {
                    $("#" + standardSpecies + operationType +"StandardSpecies")[0].checked = true;
                    addRow("chargeRule" + operationType);
                }
            }
            if (status === '1' && optimizationType !== '0') {
                settingDialogDiv.find("#standardSpecies" + operationType + " label").css("display", "block");
                settingDialogDiv.find("#standardSpecies" + operationType + " input").css("display", "block");
                settingDialogDiv.find("#chargeRule" + operationType).css("display", "block");
            } else {
                settingDialogDiv.find("#standardSpecies" + operationType + " label").css("display", "none");
                settingDialogDiv.find("#standardSpecies" + operationType + " input").css("display", "none");
                settingDialogDiv.find("#chargeRule" + operationType).css("display", "none");
            }
        } else {
            clearInfo(operationType);
            if (isSEO === "false") {
                groupObj.find("input:checked").each(function () {
                    $("#changeSettingDialog").find("#" + $(this).val() + operationType + "StandardSpecies").prop("checked", false);
                });
                clearStandardInfo(operationType, standardSpecies, 1);
            }
            groupObj.css("display","none");
        }
    }
}

function getAvailableQZSettings() {
    $.ajax({
        url:'/internal/qzsetting/getAvailableQZSettings',
        headers:{
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        timeout:'5000',
        type:'POST',
        success: function (data) {
            var text = "";
            $.each(data,function(index,element) {
                text += element.domain;
                if(element.updateEndTime != null && element.updateEndTime != ''){
                    text += '______'+ toTimeFormat(new Date(element.updateEndTime));
                }
                text += '\r';
            });
            $("#getAvailableQZSettingsContent").val(text);
        }
    });
    $("#getAvailableQZSettings").show();
    $("#getAvailableQZSettings").dialog({
        resizable: false,
        height: 450,
        width: 340,
        title: '查看更新列队',
        modal: true,
        buttons: [{
            text: '确定',
            iconCls: 'icon-ok',
            handler: function () {
                $("#getAvailableQZSettings").dialog("close")
            }
        }]
    });
    $("#getAvailableQZSettings").dialog("open");
    $("#getAvailableQZSettings").window("resize",{top:$(document).scrollTop() + 100});
}

function showExcludeCustomerKeywordDialog(qzSettingUuid, customerUuid, domain, optimizedGroupName, terminalType) {
    var excludeCustomerKeywordDialog = $("#excludeCustomerKeywordDialog");
    excludeCustomerKeywordDialog.find('#excludeCustomerKeywordForm')[0].reset();
    excludeCustomerKeywordDialog.find("#qzSettingUuid").val(qzSettingUuid);
    excludeCustomerKeywordDialog.find("#customerUuid").val(customerUuid);
    excludeCustomerKeywordDialog.find("#terminalType").val(terminalType);
    excludeCustomerKeywordDialog.find("#domain").val(domain.trim());
    echoExcludeKeyword();
    excludeCustomerKeywordDialog.show();
    excludeCustomerKeywordDialog.dialog({
        resizable: false,
        height: 450,
        width: 340,
        title: '排除关键字',
        modal: false,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                excludeCustomerKeywords(qzSettingUuid, customerUuid, domain, optimizedGroupName);
            }
        }, {
            text: '取消',
            iconCls: 'icon-cancel',
            handler: function () {
                $("#excludeCustomerKeywordDialog").dialog("close");
                $('#excludeCustomerKeywordForm')[0].reset();
            }
        }]
    });
    excludeCustomerKeywordDialog.dialog("open");
    excludeCustomerKeywordDialog.window("resize",{top:$(document).scrollTop() + 100});
}
function excludeCustomerKeywords(qzSettingUuid, customerUuid, domain, optimizedGroupName) {
    var excludeCustomerKeywordDialog = $("#excludeCustomerKeywordDialog");
    var terminalType = excludeCustomerKeywordDialog.find("#terminalType").val();
    var excludeKeywordUuid = excludeCustomerKeywordDialog.find("#excludeKeywordUuid").val();
    var keywordStr = excludeCustomerKeywordDialog.find("#customerKeywordDialogContent").val();
    if (keywordStr === "") {
        $.messager.alert('提示', '请输入关键字！！', 'warning');
        excludeCustomerKeywordDialog.find("#customerKeywordDialogContent").focus();
        return false;
    }
    keywordStr = keywordStr.replace(/[，|\r\n]/g, ",").replace(/[\s+]/g, "");
    if (keywordStr.substring(keywordStr.length - 1) === ','){
        keywordStr = keywordStr.substring(0, keywordStr.length - 1);
    }
    var keywords = keywordStr.split(',');
    keywords = keywords.filter(function (keyword, index) {
        return keywords.indexOf(keyword) === index && keyword !== '';
    });
    var postData = {};
    postData.excludeKeywordUuid = excludeKeywordUuid;
    postData.qzSettingUuid = qzSettingUuid;
    postData.customerUuid = customerUuid;
    postData.domain = $.trim(domain);
    postData.optimizeGroupName = optimizedGroupName;
    postData.terminalType = $.trim(terminalType);
    postData.keywords = keywords;
    $.ajax({
        url: '/internal/qzsetting/excludeQZSettingCustomerKeywords',
        type: 'POST',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (data) {
            if (data) {
                $().toastmessage('showSuccessToast', "保存成功！");
                $("#excludeCustomerKeywordDialog").dialog("close");
            } else {
                $().toastmessage('showErrorToast', "保存失败！");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "保存失败！");
        }
    });
}
function echoExcludeKeyword() {
    var excludeCustomerKeywordDialog = $("#excludeCustomerKeywordDialog");
    var qzSettingUuid = excludeCustomerKeywordDialog.find("#qzSettingUuid").val();
    var terminalType = excludeCustomerKeywordDialog.find("#terminalType").val();
    var postData = {};
    postData.qzSettingUuid = qzSettingUuid;
    postData.terminalType = $.trim(terminalType);
    $.ajax({
        url: '/internal/qzsetting/echoExcludeKeyword',
        type: 'POST',
        data: JSON.stringify(postData),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (data) {
            if (data != null && data !== "") {
                data.keyword = data.keyword.replace(/[,]/g, "\n");
                $("#excludeCustomerKeywordDialog").find("#excludeKeywordUuid").val(data.uuid);
                $("#excludeCustomerKeywordDialog").find("#customerKeywordDialogContent").val(data.keyword);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "显示已添加排除词失败！");
        }
    });
}

function checkedStandardSpecies(self, terminalType) {
    var inputValue = $(self).val();
    if ($(self)[0].checked) {
        $(self).parent().parent().find("input:checked").each(function () {
            if ($(this).val() !== inputValue) {
                $("#changeSettingDialog").find("#" + $(this).val() + terminalType + "StandardSpecies").prop("checked", false);
            } else {
                clearStandardInfo(terminalType, inputValue, 0);
            }
        });
        $("#chargeRule" + terminalType).css("display", "block");
        addRow("chargeRule" + terminalType);
    }  else {
        $(self)[0].checked = true;
        $().toastmessage('showErrorToast', '必须选中一种达标种类！！')
    }
}

function showOptimizationType(terminalType) {
    var settingDialogDiv = $("#changeSettingDialog");
    var groupObj = settingDialogDiv.find("#" + terminalType);
    var divObj = settingDialogDiv.find("#optimizationType" + terminalType);
    if (groupObj[0].checked === true) {
        divObj.css("display", "block");
    } else {
        divObj.css("display", "none");
        $(divObj).find("input[name='optimizationType']:checkbox:checked").each(function () {
            $(this).prop("checked", false);
            dealSettingTable(this, terminalType, 0);
        });
    }
}

function changeQZSettingGroupOperationCombineUuid(self, groupName, userName, isSEO) {
    var select = $(self);
    var oldOperationCombineName = select.parent().find("input[name='operationCombineName']").val();
    if (isSEO === 'true' || userName !== '') {
        $().toastmessage('showErrorToast', '您无权更改操作组合');
        select.val(oldOperationCombineName);
        return false;
    }
    parent.$.messager.confirm('确认', "确定修改" + groupName + "所属的操作组合？", function (b) {
        if (b) {
            var postData = {};
            var newOperationCombineName = select.val();
            postData.operationCombineUuid = newOperationCombineName === '' ? null : newOperationCombineName.split("_____")[1];
            postData.groupName = groupName;
            $.ajax({
                url: '/internal/group/updateQZSettingGroupOperationCombineUuid',
                type: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(postData),
                success: function (result) {
                    if (result) {
                        $().toastmessage('showSuccessToast', "更新操作组合成功");
                        select.parent().find("input[name='operationCombineName']").val(newOperationCombineName);
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', '更新操作组合失败');
                    select.val(oldOperationCombineName);
                }
            });
        } else {
            select.val(oldOperationCombineName);
        }
    });
}



/**
 * 批量修改关键字机器分组
 * @param changeType
 */
function updateQzCategoryTags() {
    var uuids = getSelectedIDs();
    if(uuids === ''){
        alert("请选择要操作的站点!!")
        return false;
    }
    $("#targetQzCategoryTagsDialog").css("display", "block");
    $("#targetQzCategoryTagsDialog").dialog({
        resizable: false,
        width: 320,
        height: 100,
        title:"修改站点分组标签",
        closed: true,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                var qzSettingSearchCriteria = {};
                qzSettingSearchCriteria.customerUuids = uuids;
                qzSettingSearchCriteria.targetQZCategoryTags =[];
                var qzCategoryTagNames = $("#targetQzCategoryTagsFrom").find("#targetQzCategoryTags").val().replace(/(，)+/g, ",");
                if (qzCategoryTagNames !== "") {
                    var tagNameArr = qzCategoryTagNames.split(",");
                    tagNameArr = unique(tagNameArr);
                    $.each(tagNameArr, function (idx, val) {
                        //防止多打了分号或者分组名为空格串导致存入分组名为空的数据
                        if($.trim(val)!=""){
                            var qzCategoryTag = {};
                            qzCategoryTag.tagName = $.trim(val);
                            qzSettingSearchCriteria.targetQZCategoryTags.push(qzCategoryTag);
                        }
                });
                }else{
                    $.messager.alert('提示', '请输入分组标签!!', 'info');
                    return false;
                }



                $.ajax({
                    url: '/internal/qzsetting/updateQzCategoryTags',
                    data: JSON.stringify(qzSettingSearchCriteria),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (data) {
                        if(data){
                            $().toastmessage('showSuccessToast',"操作成功", true);
                        }else{
                            $().toastmessage('showErrorToast', "操作失败");
                        }
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "操作失败");
                    }
                });
                $("#targetMachineGroupDialog").dialog("close");
            }
        },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $("#targetQzCategoryTagsDialog").dialog("close");
                    $('#targetQzCategoryTagsFrom')[0].reset();
                }
            }]
    });
    $("#targetQzCategoryTagsDialog").dialog("open");
    $('#targetQzCategoryTagsDialog').window("resize",{top:$(document).scrollTop() + 200});
}


