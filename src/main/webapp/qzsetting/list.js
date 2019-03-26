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
    loadingCheckTerminalType();
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
function loadingCheckTerminalType() {
    var terminalType = $("#chargeForm").find("#terminalType").val();
    checkTerminalType(terminalType, false);
}
function searchRiseOrFall() {
    $(".mytabs div:eq(0)").find("input:checkbox").click(function () {
        var checkStatus;
        if (!$(this).prop("checked")) {
            checkStatus = null;
        } else {
            var parentName = $(this).parent().attr("name");
            if (parentName == "lower") {
                checkStatus = 1;
            }
            if (parentName == "upper") {
                checkStatus = 2;
            }
            if (parentName == "atLeastStandard") {
                checkStatus = 3;
            }
            if (parentName == "neverStandard") {
                checkStatus = 4;
            }
            if (parentName == "closeStandard") {
                checkStatus = 5;
            }
            if (parentName == "unchanged") {
                checkStatus = 6;
            }
            if (parentName =="lowerDifference") {
                checkStatus = 7;
            }
            if (parentName =="unchangedDifference") {
                checkStatus = 8;
            }
            if (parentName =="upperDifference") {
                checkStatus = 9;
            }
        }
        $("#chargeForm").find("#checkStatus").val(checkStatus);
        trimSearchCondition('1');
    });
}
function detectedMoreSearchConditionDivShow() {
    var moreSearchCondition = $("div.conn[name='moreSearchCondition']");
    var customerInfo = moreSearchCondition.find("ul li.customerInfo input").val();
    var categoryTag = moreSearchCondition.find("ul li.category input").val();
    var group =  moreSearchCondition.find("ul li.group input").val();
    var status = moreSearchCondition.find("select[name='status']").val();
    var updateStatus = moreSearchCondition.find("select[name='updateStatus']").val();
    var baiduWeight = moreSearchCondition.find("select[name='weight']").val();
    var values = customerInfo + categoryTag + group + status + updateStatus + baiduWeight;
    if (values != "") {
        moreSearchCondition.css("display", "block");
    }
}
function checkTerminalType(terminalType, isManualSwitch) {
    var a = $(".mytabs .link").find("li.active a");
    if (a[0] != undefined && a[0].innerHTML.substring(2) == terminalType) {
        return;
    }
    $(".mytabs .link").find("li").removeClass("active");
    if (terminalType == "PC") {
        $(".mytabs .link").find("li[name='PC']").addClass("active");
        $("#chargeForm").find("#terminalType").val($.trim(terminalType));
    }
    if (terminalType == "Phone") {
        $(".mytabs .link").find("li[name='Phone']").addClass("active");
        $("#chargeForm").find("#terminalType").val($.trim(terminalType));
    }
    if (isManualSwitch) {
        trimSearchCondition('1');
    }
    setTimeout(function (){
        detectedTopNum();
    }, 200);
    setTimeout(function () {
        getQZSettingClientGroupInfo(terminalType);
    }, 100);
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
}
function generateQZKeywordRecordCharts(domElement, data) {
    if (domElement == undefined) {
        return;
    }
    if (JSON.parse(data).date == '') {
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
            text: '百度收录趋势',
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
    if (domElement == undefined) {
        return;
    }
    if (JSON.parse(data).date == '') {
        domElement.innerHTML = "<h1 style='text-align: center'> 暂无数据 </h1>";
        return;
    }
    var result = JSON.parse(data);
    var date = result.date.replace("['", "").replace("']", "").split("', '").reverse();
    var topTen = stringToArray(result.topTen);
    var topTwenty = stringToArray(result.topTwenty);
    var topThirty = stringToArray(result.topThirty);
    var topForty = stringToArray(result.topForty);
    var topFifty = stringToArray(result.topFifty);
    var keywordTrendCharts = echarts.init(domElement);
    var option = {
        color: ['#228B22', '#0000FF', '#FF6100', '#000000', '#FF0000'],
        title : {
            text: '关键词排名趋�,
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
            name: '�0�,
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
            name: '�0�,
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
            name: '�0�,
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
            name: '�0�,
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
            name: '�0�,
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
    keywordTrendCharts.setOption(option);
}
function stringToArray(str) {
    return str.replace('[', '').replace(']', '').split(', ').reverse();
}
function getQZSettingClientGroupInfo(terminalType) {
    $(".body").find(".other-rank_2").each(function () {
        var div = $(this);
        var uuid = div.parent().parent().parent().find(".header input[name='uuid']").val();
        var optimizeGroupName = div.find(".row:first-child").find("div:eq(0) span.line1 a").text();
        if (optimizeGroupName.indexOf("(") == -1) {
            optimizeGroupName = $.trim(optimizeGroupName);
        } else {
            optimizeGroupName = $.trim(optimizeGroupName.substring(0,optimizeGroupName.indexOf("(")));
        }
        var postData = {};
        postData.qzSettingUuid = uuid;
        postData.terminalType = terminalType;
        postData.type = $.trim(div.parent().find(".other-rank .row:last-child").find("div:eq(2) span.line1 input[name='type']").val());
        postData.optimizeGroupName = optimizeGroupName;
        $.ajax({
            url: '/internal/qzsetting/getQZSettingClientGroupInfo',
            type: 'POST',
            data: JSON.stringify(postData),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (data) {
                div.parent().find(".other-rank .row:first-child").find("div[name='operationKeywordNum']").find("span.line1 a").text(data.customerKeywordCount);
                var clientCount = 0;
                var showSomeOperationType = div.find(".row:last-child").find("div[name='showSomeOperationType']");
                if (data.categoryTagNames.length > 0) {
                    var tagNameStr = "";
                    var span = $(div).parent().parent().parent().find(".header span.tagNames");
                    $.each(data.categoryTagNames, function (idx, val) {
                        tagNameStr += val + ",";
                    });
                    $(span).find("label.tagNameStr").html(tagNameStr.substring(0, tagNameStr.length-1));
                }
                if (data.clientStatusVOs.length > 0) {
                    showSomeOperationType.empty();
                    var allOperationType = '';
                    var flag = false;
                    $.each(data.clientStatusVOs, function (idx, val) {
                        allOperationType += optimizeGroupName + " " +val.operationType + "(" + val.operationTypeCount + ")"  + ",";
                        clientCount += val.operationTypeCount;
                        if (idx < 2) {
                            $(showSomeOperationType).append("<span name='"+ optimizeGroupName + " " + val.operationType +"'><a href='javascript:;' onclick='findOptimizeGroupAndOperationType($(this))'>"+ val.operationType + "(" + val.operationTypeCount + ")" +"</a></span>");
                        } else {
                            flag = true;
                        }
                    });
                    if (flag) {
                        $(showSomeOperationType).append("<span><a name='showAllOperationType' href='javascript:;' onclick='showAllOperationType($(this))'><strong> . . . </strong></a></span>");
                        allOperationType = allOperationType.substring(0, allOperationType.length-1);
                        $(showSomeOperationType).parent().find("input[name='allOperationType']").val(allOperationType);
                    }
                }
                var status = div.parent().find(".other-rank .row:last-child").find("div:eq(3) span.line1 a").attr("status");
                div.find(".row:first-child").find("div:eq(0) span.line1 a").text(optimizeGroupName+" ("+clientCount+")");
                if (status == "3") {
                    div.find(".row:first-child").find("div:eq(0) span.line1 a").css("color", "red");
                }
            },
            error: function () {
                console.log("获取优化分组机器信息失败，请刷新重试或提交问题给开发人员！");
            }
        });
    });
}
function editTagNameStr(o, edit){
    if (edit) {
        o.innerHTML = o.innerHTML.replace(/(暂无)/g, '');
        var uuid = $(o).parent().parent().find("input[name='uuid']").val();
        o.innerHTML = '<input type="text" label="'+ o.innerHTML +'" uuid="'+ uuid +'" style="width: 400px;" value="' + o.innerHTML + '" onblur="editTagNameStr(this)">';
        o.getElementsByTagName('input')[0].focus();
    } else {
        var isChange = true;
        var qzCategoryTags = [];
        var categoryTagNames = o.value.replace(/( )+/g,"").replace(/(�+|(,)+/g, ",").split(",");
        categoryTagNames = unique(categoryTagNames);
        if (o.value != "") {
            o.value = "";
            $.each(categoryTagNames, function (idx, val) {
                if (val != "") {
                    var qzCategoryTag = {};
                    qzCategoryTag.tagName = $.trim(val);
                    qzCategoryTags.push(qzCategoryTag);
                    o.value += val + ",";
                }
            });
            o.value = o.value.substring(0, o.value.length-1);
        }
        var label = $(o).attr("label");
        if ($.trim(o.value) == $.trim(label)) {
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
                        $().toastmessage('showSuccessToast', "保存成功�);
                    } else {
                        $().toastmessage('showErrorToast', "保存失败�);
                        o.value = $.trim(label);
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "保存失败�);
                    o.value = $.trim(label);
                }
            });
        }
        setTimeout(function(){
            if ($.trim(o.value) == "") {
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
    var customerUuid = customerInfo.substr(customerInfo.lastIndexOf("_") + 1);
    chargeForm.find("#customerInfo").val($.trim(customerInfo));
    chargeForm.find("#customerUuid").val(customerUuid);
    chargeForm.find("#dateRangeType").val(days);

    var domain = $(".conn").find("li:first-child input[name='domain']").val();
    var categoryTag = $(".conn").find(".category").find("input[name='categoryTag']").val();
    var group = $(".conn").find(".group").find("input[name='group']").val();
    var status = $(".conn").find("select[name='status']").val();
    var updateStatus = $(".conn").find("select[name='updateStatus']").val();
    var baiduWeight = $(".conn").find("select[name='weight']").val();
    chargeForm.find("#domain").val($.trim(domain));
    chargeForm.find("#categoryTag").val($.trim(categoryTag));
    chargeForm.find("#group").val($.trim(group));
    if (status != "") {
        chargeForm.find("#status").val($.trim(status));
    } else {
        chargeForm.find("#status").val(null);
    }
    if (updateStatus != "") {
        chargeForm.find("#updateStatus").val($.trim(updateStatus));
    } else {
        chargeForm.find("#updateStatus").val(null);
    }
    if (baiduWeight != "") {
        chargeForm.find("#baiduWeight").val($.trim(baiduWeight));
    } else {
        chargeForm.find("#baiduWeight").val(null);
    }
    chargeForm.submit();
}
function showMoreSearchCondition() {
    $(".mytabs").find("div[name='moreSearchCondition']").toggle();
    $.ajax({
        url: "/internal/qzcategorytag/getAllCategoryTagNames",
        type: "GET",
        success: function (categoryTagNames) {
            $("#categoryTag_list").find('option').remove();
            if (categoryTagNames != null) {
                $.each(categoryTagNames, function (idx, val) {
                    $("#categoryTag_list").append("<option value='" + val.tagName + "'></option>")
                });
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取标签信息失败�);
        }
    });
}
function searchClientStatus(optimizeGroup, operationType) {
    var searchClientStatusFrom = $("#searchClientStatusForm");
    searchClientStatusFrom.find("#groupName").val($.trim(optimizeGroup));
    if (operationType != null) {
        searchClientStatusFrom.find("#operationType").val($.trim(operationType));
    }
    searchClientStatusFrom.submit();
}
function searchCustomerKeywords(customerUuid, optimizeGroupName) {
    var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
    searchCustomerKeywordForm.find("#customerUuid").val(customerUuid);
    searchCustomerKeywordForm.find("#optimizeGroupName").val(optimizeGroupName);
    searchCustomerKeywordForm.find("#status").val(1);
    searchCustomerKeywordForm.submit();
}
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
    var qzSettingUuid = $(self).attr("id");
    var terminalType = $("#chargeForm").find("#terminalType").val();
    $("#chargeRulesDivTable  tr:not(:first)").remove();
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
        success: function (qzChargeRules) {
            $("#chargeRulesDivTable  tr:not(:first)").remove();
            if(qzChargeRules != null && qzChargeRules.length > 0) {
                var achieveLevel = $(self).attr("level");
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
                    if (idx + 1 === parseInt(achieveLevel)) {
                        $(newTr).css("background-color", "green");
                    }
                    $("#chargeRulesDivTable")[0].lastChild.appendChild(newTr);
                });
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败�);
        }
    });
    var chargeRulesDiv = document.getElementById('chargeRulesDiv');
    chargeRulesDiv.style.display="block";
    chargeRulesDiv.style.left=(pageX) + "px";
    chargeRulesDiv.style.top=(pageY) + "px";
    chargeRulesDiv.style.zIndex=1000;
    chargeRulesDiv.style.position="absolute";
}
function closeChargeRulesDiv() {
    $("#chargeRulesDiv").css("display", "none");
}
function showAllOperationType(self, e) {
    var event = e||window.event;
    var pageX = event.pageX;
    var pageY = event.pageY;
    if(pageX==undefined) {
        pageX = event.clientX+document.body.scrollLeft||document.documentElement.scrollLeft;
    }
    if(pageY==undefined) {
        pageY = event.clientY+document.body.scrollTop||document.documentElement.scrollTop;
    }
    var allOperationType = $(self).parent().parent().parent().find("input[name='allOperationType']").val();
    var showAllOperationType = $("#showAllOperationType");
    showAllOperationType.empty();
    var operationTypes = allOperationType.split(',');
    $.each(operationTypes, function (idx, val) {
        showAllOperationType.append("<span name='"+ val.substring(0, val.indexOf("(")) +"'>" + "<a href='javascript:;' onclick='findOptimizeGroupAndOperationType($(this))'>" + val.split(" ")[1] + "</a>" + "</span>" + "<br>");
    });
    showAllOperationType.show();
    showAllOperationType.dialog({
        resizable: false,
        height: 300,
        width: 200,
        title: '查看操作类型',
        modal: false,
        buttons: [{
            text: '确定',
            iconCls: 'icon-ok',
            handler: function () {
                showAllOperationType.dialog("close");
            }
        }]
    });
    showAllOperationType.dialog("open");
    showAllOperationType.window("resize",{top: pageY + 20, left: pageX - 80});
}
function findOptimizeGroupAndOperationType(self) {
    var name = $(self).parent().attr("name");
    var args = name.split(" ");
    searchClientStatus(args[0], args[1]);
}
function showKeywordDialog(qzSettingUuid, customerUuid, domain, optimizedGroupName) {
    var customerKeywordDialog = $("#customerKeywordDialog");
    customerKeywordDialog.find('#customerKeywordForm')[0].reset();
    customerKeywordDialog.find("#qzSettingUuid").val(qzSettingUuid);
    customerKeywordDialog.find("#customerUuid").val(customerUuid);
    customerKeywordDialog.find("#domain").val(domain);
    customerKeywordDialog.show();
    customerKeywordDialog.dialog({
        resizable: false,
        height: 450,
        width: 340,
        title: '指定关键�,
        modal: false,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveCustomerKeywords(qzSettingUuid, customerUuid, optimizedGroupName);
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
    customerKeywordDialog.window("resize",{top:$(document).scrollTop() + 100});
}
function saveCustomerKeywords(qzSettingUuid, customerUuid, optimizedGroupName) {
    var terminalType = $("#chargeForm").find("#terminalType").val();
    var customerKeywordDialog = $("#customerKeywordDialog");
    var domain = customerKeywordDialog.find("#domain").val();
    if (domain == '') {
        alert("请输入域�);
        customerKeywordDialog.find("#domain").focus();
        return;
    }
    var keywordStr = customerKeywordDialog.find("#customerKeywordDialogContent").val()
    if (keywordStr == "") {
        alert("请输入关键字");
        customerKeywordDialog.find("#customerKeywordDialogContent").focus();
        return;
    }
    keywordStr = keywordStr.replace(/[，|\r\n]/g, ",").replace(/[\s+]/g, "");
    if (keywordStr.substring(keywordStr.length - 1) == ','){
        keywordStr = keywordStr.substring(0, keywordStr.length - 1);
    }
    var keywords = keywordStr.split(',');
    keywords = keywords.filter(function (keyword, index) {
        return keywords.indexOf(keyword) === index && keyword != '';
    });
    var type = customerKeywordDialog.find("#qzSettingEntryType").val();
    var searchEngine = customerKeywordDialog.find("#searchEngine").val();
    var postData = {};
    postData.qzSettingUuid = qzSettingUuid;
    postData.customerUuid = customerUuid;
    postData.domain = $.trim(domain);
    postData.optimizeGroupName = optimizedGroupName;
    postData.type = type;
    postData.searchEngine = searchEngine;
    postData.terminalType = $.trim(terminalType);
    postData.keywords = keywords;
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
                $().toastmessage('showSuccessToast', "保存成功�);
                $("#customerKeywordDialog").dialog("close");
            } else {
                $().toastmessage('showErrorToast', "保存失败�);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "保存失败�);
        }
    });
}
function changePaging(currentPage, pageSize) {
    var chargeForm = $("#chargeForm");
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
function selectAll(self){
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
function decideSelectAll() {
    var a = document.getElementsByName("uuid");
    var select = 0;
    for(var i = 0; i < a.length; i++){
        if (a[i].checked == true){
            select++;
        }
    }
    if(select == a.length){
        $("#selectAllChecked").prop("checked",true);
    }else {
        $("#selectAllChecked").prop("checked",false);
    }
}
function delQZSetting(uuid) {
    if (confirm("确实要删除这个全站设置吗?") == false) return;
    $.ajax({
        url: '/internal/qzsetting/delete/' + uuid,
        type: 'Get',
        success: function (data) {
            if(data){
                $().toastmessage('showSuccessToast', "删除成功�, true);
            }else{
                $().toastmessage('showErrorToast', "删除失败");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "删除失败");
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
function delSelectedQZSettings(self) {
    var uuids = getSelectedIDs();
    if(uuids === ''){
        alert('请选择要操作的设置信息�);
        return ;
    }
    if (confirm("确实要删除这些关键字�") == false) return;
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
function updateImmediately(self) {
    var uuids = getSelectedIDs();
    if(uuids === ''){
        alert('请选择要操作的设置信息�);
        return;
    }
    if (confirm("确实要马上更新这些设置吗�) == false) return;
    var postData = {};
    postData.uuids = uuids;
    $.ajax({
        url: '/internal/qzsetting/updateImmediately',
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
function updateQZSettingStatus(status) {
    var uuids = getSelectedIDs();
    if(uuids === ''){
        alert('请选择要操作的整站�);
        return ;
    }
    if(status == 1) {
        if (confirm("确认要激活选中的整站吗�) == false) return;
    } else if (status == 2) {
        if (confirm("确认要暂停收费选中的整站吗�) == false) return;
    } else {
        if (confirm("确认要暂停选中的整站吗�) == false) return;
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
    if (selectedOperationTypes.length == 0) {
        alert("必须选择一个收费项才能收费");
        saveChargeLogFlag = false;
        return;
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
        if (chargeLog.nextChargeDate == "" || chargeLog.nextChargeDate == null) {
            alert("下次收费日期为必�);
            chargeDialog.find("#nextChargeDate" + val.id).focus();
            saveChargeLogFlag = false;
            return;
        }
        if (chargeLog.actualAmount == "" || chargeLog.actualAmount == null) {
            alert("实收金额为必�);
            chargeDialog.find("#actualAmount" + val.id).focus();
            saveChargeLogFlag = false;
            return;
        }
        if (!reg.test(chargeLog.actualAmount)) {
            alert("请输入合理的金额");
            chargeDialog.find("#actualAmount" + val.id).focus();
            saveChargeLogFlag = false;
            return;
        }
        if (chargeLog.actualChargeDate == "" || chargeLog.actualChargeDate == null) {
            alert("实际收费日期为必�);
            chargeDialog.find("#actualChargeDate" + val.id).focus();
            saveChargeLogFlag = false;
            return;
        }
        chargeLogs.push(chargeLog);
    });
    if(saveChargeLogFlag) {
        if (window.confirm("确认收费?")) {
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
                    if (data != null && data != "") {
                        $().toastmessage('showSuccessToast', "收费成功�, true);
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
            alert("取消收费");
        }
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
    chargeDialog.find("#totalAmount").html(total + "�);
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
                alert("暂无收费记录");
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败�);
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
                chargeDialogObj.find("#totalAmount").html(total+"�);
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
                $().toastmessage('showErrorToast', "获取信息失败�);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败�);
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
        height: 450,
        width: 340,
        title: '全站设置',
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveChangeSetting(this);
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
            }]
    });
    $("#changeSettingDialog").dialog("open");
    $("#changeSettingDialog").window("resize",{top:$(document).scrollTop() + 100});

}
function resetSettingDialog() {
    var settingDialogDiv = $("#changeSettingDialog");
    settingDialogDiv.find("#qzSettingUuid").val("");
    settingDialogDiv.find("#qzSettingCustomer").val("");
    settingDialogDiv.find("#qzSettingDomain").val("");
    settingDialogDiv.find("#qzCategoryTagNames").val("");
    settingDialogDiv.find("#qzSettingAutoCrawlKeywordFlag").val("1");
    settingDialogDiv.find("#qzSettingIgnoreNoIndex").val("1");
    settingDialogDiv.find("#qzSettingIgnoreNoOrder").val("1");
    settingDialogDiv.find("#qzSettingInterval").val("2");
    clearInfo("Both");
}
function clearInfo(type) {
    var settingDialogObj = $("#changeSettingDialog");
    if(type == "Both") {
        clearInfo("PC");
        clearInfo("Phone");
    } else {
        // 清空分组表格信息
        settingDialogObj.find("#group" + type).val("");
        settingDialogObj.find("#initialKeywordCount" + type).val("");
        settingDialogObj.find("#currentKeywordCount" + type).val("");
        settingDialogObj.find("#qzOperationTypeUuid" + type).val("");
        // 清空规则表格信息
        settingDialogObj.find("#chargeRule" + type).find("tr:not(:first,:last)").remove();
        settingDialogObj.find("#" + type)[0].checked = false;
        settingDialogObj.find("#operationTypeSummaryInfo" + type).css("display","none");
        settingDialogObj.find("#chargeRule" + type).css("display","none");
    }
}
function showSettingDialog(uuid, self) {
    resetSettingDialog();
    if(uuid == null){
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
                $().toastmessage('showErrorToast', "获取信息失败�);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败�);
        }
    });
}
function initSettingDialog(qzSetting, self) {
    var PCType = false;
    var PhoneType = false;
    var settingDialogDiv = $("#changeSettingDialog");
    settingDialogDiv.find("#qzSettingUuid").val(qzSetting.uuid);
    settingDialogDiv.find("#bearPawNumber").val(qzSetting.bearPawNumber);
    settingDialogDiv.find("#qzSettingCustomer").val(
        qzSetting.contactPerson + "_____" + qzSetting.customerUuid);
    settingDialogDiv.find("#qzSettingDomain").val(
        qzSetting.domain != null ? qzSetting.domain : "");
    settingDialogDiv.find("#qzSettingAutoCrawlKeywordFlag").val(qzSetting.autoCrawlKeywordFlag ? "1" : "0");
    settingDialogDiv.find("#qzSettingIgnoreNoIndex").val(qzSetting.ignoreNoIndex ? "1" : "0");
    settingDialogDiv.find("#qzSettingIgnoreNoOrder").val(qzSetting.ignoreNoOrder ? "1" : "0");
    settingDialogDiv.find("#qzSettingInterval").val(
        qzSetting.updateInterval != null ? qzSetting.updateInterval : "");
    settingDialogDiv.find("#qzSettingEntryType").val(
        qzSetting.type != null ? qzSetting.type : "");
    // 操作类型表填充数�
    $.each(qzSetting.qzOperationTypes, function (idx, val) {
        settingDialogDiv.find("#group" + val.operationType).val(val.group);
        settingDialogDiv.find("#subDomainName" + val.operationType).val(val.subDomainName);
        settingDialogDiv.find("#initialKeywordCount" + val.operationType).val(
            val.initialKeywordCount);
        settingDialogDiv.find("#currentKeywordCount" + val.operationType).val(
            val.currentKeywordCount);
        /* 限制最大词�*/
        settingDialogDiv.find("#maxKeywordCount" + val.operationType).val(val.maxKeywordCount);
        settingDialogDiv.find("#qzSettingUuid" + val.operationType).val(val.uuid);
        // 构造规则表
        $.each(val.qzChargeRules, function (chargeRuleIdx, chargeRuleVal) {
            addRow("chargeRule" + val.operationType, chargeRuleVal);
            if (val.operationType === 'PC') {
                PCType = true;
            }
            if (val.operationType === 'Phone') {
                PhoneType = true;
            }
        });
    });
    // 分类标签
    var tagNames = "";
    $.each(qzSetting.qzCategoryTags, function (idx, val) {
        tagNames += val.tagName + ",";
    });
    settingDialogDiv.find("#qzCategoryTagNames").val(tagNames.substring(0,tagNames.length-1));
    if (PCType) {
        dealSettingTable("PC");
    }

    if (PhoneType) {
        dealSettingTable("Phone");
    }
}
//规则表验�
var reg = /^[1-9]\d*$/;
function saveChangeSetting(self) {
    var settingDialogDiv = $("#changeSettingDialog");
    var qzSetting = {};
    qzSetting.uuid = settingDialogDiv.find("#qzSettingUuid").val();

    var customer = settingDialogDiv.find("#qzSettingCustomer").val();
    if (customer == null || customer === "") {
        alert("请选择客户");
        settingDialogDiv.find("#qzSettingCustomer").focus();
        return;
    }
    qzSetting.domain = settingDialogDiv.find("#qzSettingDomain").val();
    if (qzSetting.domain == null || qzSetting.domain === "") {
        alert("请输入域�);
        settingDialogDiv.find("#qzSettingDomain").focus();
        return;
    }
    qzSetting.bearPawNumber = settingDialogDiv.find("#bearPawNumber").val();
    qzSetting.autoCrawlKeywordFlag = settingDialogDiv.find("#qzSettingAutoCrawlKeywordFlag").val() === "1" ? true : false;
    qzSetting.ignoreNoIndex = settingDialogDiv.find("#qzSettingIgnoreNoIndex").val() === "1" ? true : false;
    qzSetting.ignoreNoOrder = settingDialogDiv.find("#qzSettingIgnoreNoOrder").val() === "1" ? true : false;
    qzSetting.updateInterval = settingDialogDiv.find("#qzSettingInterval").val();
    qzSetting.pcGroup = settingDialogDiv.find("#groupPC").val();
    qzSetting.phoneGroup = settingDialogDiv.find("#groupPhone").val();
    if(qzSetting.pcGroup == "") {
        qzSetting.pcGroup = null;
    }
    if(qzSetting.phoneGroup == "") {
        qzSetting.phoneGroup = null;
    }

    if (customer != null && customer != '') {
        var customerArray = customer.split("_____");
        if (customerArray.length == 2) {
            qzSetting.customerUuid = customerArray[1];
        } else {
            alert("请从列表中选择客户");
            settingDialogDiv.find("#qzSettingCustomer").focus();
            return;
        }
    }
    var entryType = settingDialogDiv.find("#qzSettingEntryType").val();
    qzSetting.type = entryType;
    qzSetting.qzOperationTypes = [];//操作类型�
    qzSetting.qzOperationTypes.qzChargeRules = [];//收费规则
    qzSetting.qzCategoryTags = []; //分类标签�

    var tagNames = settingDialogDiv.find("#qzCategoryTagNames").val().replace(/(�+/g, ",");
    if (tagNames != "") {
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
        var ruleObj = settingDialogDiv.find("#chargeRule" + val.id);
        var operationType = {};
        operationType.qzChargeRules = [];
        operationType.operationType = val.id;
        operationType.group = settingDialogDiv.find("#group" + val.id).val();
        operationType.initialKeywordCount = settingDialogDiv.find(
            "#initialKeywordCount" + val.id).val();
        operationType.currentKeywordCount = settingDialogDiv.find(
            "#currentKeywordCount" + val.id).val();
        operationType.maxKeywordCount = settingDialogDiv.find("#maxKeywordCount" + val.id).val();
        operationType.subDomainName = settingDialogDiv.find("#subDomainName" + val.id).val();
        if (operationType.group == null || operationType.group === "") {
            alert("请输入分�);
            settingDialogDiv.find("#group" + val.id).focus();
            validationFlag = false;
            return false;
        }else if(operationType.group.length>20){
            alert(operationType.operationType+"分组大于20位，请重新输�);
            settingDialogDiv.find("#group" + val.id).focus();
            validationFlag = false;
            return false;
        }
//        if (operationType.initialKeywordCount == null || operationType.initialKeywordCount === "") {
//          alert("请输入初始词�);
//          settingDialogDiv.find("#initialKeywordCount" + val.id).focus();
//          validationFlag = false;
//          return false;
//        }
        if (parseInt(operationType.initialKeywordCount) < 0 && (operationType.initialKeywordCount == "" || !reg.test(operationType.initialKeywordCount))) {
            alert("请输入正整数");
            settingDialogDiv.find("#initialKeywordCount" + val.id).focus();
            validationFlag = false;
            return false;
        }
        if (qzSetting.autoCrawlKeywordFlag && (operationType.maxKeywordCount == "" || !reg.test(operationType.maxKeywordCount))){
            alert("请输入PC限制词量");
            settingDialogDiv.find("#maxKeywordCount" + val.id).focus();
            validationFlag = false;
            return false;
        }
        //多条规则
        var endKeyWordCountValue = -1;
        var trObjs = ruleObj.find("tr:not(:first,:last)");
        $.each(trObjs, function (idx, val) {
            var startKeywordCountObj = $(val).find("input[name=startKeywordCount]");
            var endKeywordCountObj = $(val).find("input[name=endKeywordCount]");
            var amountObj = $(val).find("input[name=amount]");

            var chargeRule = {};
            chargeRule.startKeywordCount = startKeywordCountObj.val();
            chargeRule.endKeywordCount = endKeywordCountObj.val();
            chargeRule.amount = amountObj.val();
            operationType.qzChargeRules.push(chargeRule);

            if (startKeywordCountObj.val() == null || startKeywordCountObj.val() == "") {
                alert("请输入起始词�);
                startKeywordCountObj[0].focus();
                validationFlag = false;
                return false;
            }
            if (!reg.test(startKeywordCountObj.val())) {
                alert("请输入数�);
                startKeywordCountObj.focus();
                validationFlag = false;
                return false;
            }
            var skc = Number(startKeywordCountObj.val());
            if (skc <= endKeyWordCountValue) {
                alert("起始词数过小");
                startKeywordCountObj.focus();
                validationFlag = false;
                return false;
            }
            if (idx < (trObjs.length - 1)) {
                if (endKeywordCountObj.val() == null || endKeywordCountObj.val() == "") {
                    alert("请输入终止词�);
                    endKeywordCountObj.focus();
                    validationFlag = false;
                    return false;
                }
            } else {
                if(endKeywordCountObj.val() != "" && operationType.currentKeywordCount>Number(endKeywordCountObj.val())){
                    alert("最后一条规则中的终止词量必须大于当前词�);
                    endKeywordCountObj.focus();
                    validationFlag = false;
                    return false;
                }
            }
            if (endKeywordCountObj.val() != "") {
                if (!reg.test(endKeywordCountObj.val())) {
                    alert("请输入数�);
                    endKeywordCountObj.focus();
                    validationFlag = false;
                    return false;
                }
                if (Number(endKeywordCountObj.val()) <= skc) {
                    alert("终止词数必须大于起始词数");
                    endKeywordCountObj.focus();
                    validationFlag = false;
                    return false;
                }
            }
            if (amountObj.val() == null || amountObj.val() == "") {
                alert("请输入价�);
                amountObj.focus();
                validationFlag = false;
                return false;
            }
            if (!reg.test(amountObj.val())) {
                alert("输入的价格不合理");
                amountObj.focus();
                validationFlag = false;
                return false;
            }
            endKeyWordCountValue = Number(endKeywordCountObj.val());
        });
        if (!validationFlag) {
            return false;
        }
        qzSetting.qzOperationTypes.push(operationType);
    });

    if (validationFlag) {
        if (checkedObjs.length == 0) {
            alert("保存失败，必须要增加一条规�);
            return;
        }
        $.ajax({
            url: '/internal/qzsetting/save',
            data: JSON.stringify(qzSetting),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (data) {
                if (data != null && data != "") {
                    $().toastmessage('showSuccessToast', "更新成功", true);
                } else {
                    $().toastmessage('showErrorToast', "更新失败�);
                }
                $("#changeSettingDialog").dialog("close");
            },
            error: function () {
                $().toastmessage('showErrorToast', "更新失败�);
            }
        });
    }
}
function addRow(tableID){
    addRow(tableID, null);
}
function addRow(tableID, chargeRule){
    var tableObj = $("#" + tableID);
    var rowCount = tableObj.find("tr").length;
    var newRow = tableObj[0].insertRow(rowCount - 1); //插入新行

    var col1 = newRow.insertCell(0);
    col1.innerHTML="<input type='text' name='sequenceID' value='"+(rowCount - 1)+"' style='width:100%'/>";
    var col2 = newRow.insertCell(1);
    col2.innerHTML = "<input type='text' name='startKeywordCount' value='"+(chargeRule != null ? chargeRule.startKeywordCount : '')+"' style='width:100%'/>";
    var col3 = newRow.insertCell(2);
    col3.innerHTML = "<input type='text' name='endKeywordCount' value='"+((chargeRule != null && chargeRule.endKeywordCount != null) ? chargeRule.endKeywordCount : '')+"'  style='width:100%'/>";
    var col4 = newRow.insertCell(3);
    col4.innerHTML = "<input type='text' name='amount' value='"+(chargeRule != null ? chargeRule.amount : '')+"'  style='width:100%'/>";
    var col5 = newRow.insertCell(4);
    col5.innerHTML = "<input style='width:100%' type='button' value='删除' onclick='deleteCurrentRow(this.parentNode.parentNode)' />";
}
function deleteCurrentRow(currentRow) {
    var index = currentRow.rowIndex;
    var tableObj = currentRow.parentNode.parentNode;
    if(tableObj.rows.length > 3) {
        tableObj.deleteRow(index);
        $.each($("#"+tableObj.id).find("input[name=sequenceID]"), function(idx, val){
            $(val).val(idx + 1);
        });
    } else {
        alert("删除失败，规则表不允许为�);
    }
}
function dealSettingTable(operationType) {
    var settingDialogDiv = $("#changeSettingDialog");
    var groupObj = settingDialogDiv.find('#operationTypeSummaryInfo' + operationType);
    var ruleObj = settingDialogDiv.find('#chargeRule' + operationType);
    var chargeRuleRowCount = ruleObj.find("tr").length;
    var checkboxObj = settingDialogDiv.find('#' + operationType);

    if (ruleObj.css("display") == "none" || checkboxObj[0].checked == true) {
        // 保证必须有一条规�
        if (chargeRuleRowCount <= 2) {
            addRow("chargeRule" + operationType);
        }
        groupObj.css("display","block");
        ruleObj.css("display","block");
        checkboxObj[0].checked = true;
    } else {
        clearInfo(operationType);
        groupObj.css("display","none");
        ruleObj.css("display","none");
        checkboxObj[0].checked = false;
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
    excludeCustomerKeywordDialog.find("#domain").val(domain);
    echoExcludeKeyword();
    excludeCustomerKeywordDialog.show();
    excludeCustomerKeywordDialog.dialog({
        resizable: false,
        height: 450,
        width: 340,
        title: '排除关键�,
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
    if (keywordStr == "") {
        alert("请输入关键字");
        excludeCustomerKeywordDialog.find("#customerKeywordDialogContent").focus();
        return;
    }
    keywordStr = keywordStr.replace(/[，|\r\n]/g, ",").replace(/[\s+]/g, "");
    if (keywordStr.substring(keywordStr.length - 1) == ','){
        keywordStr = keywordStr.substring(0, keywordStr.length - 1);
    }
    var keywords = keywordStr.split(',');
    keywords = keywords.filter(function (keyword, index) {
        return keywords.indexOf(keyword) === index && keyword != '';
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
                $().toastmessage('showSuccessToast', "保存成功�);
                $("#excludeCustomerKeywordDialog").dialog("close");
            } else {
                $().toastmessage('showErrorToast', "保存失败�);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "保存失败�);
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
            if (data != null) {
                data.keyword = data.keyword.replace(/[,]/g, "\n");
                $("#excludeCustomerKeywordDialog").find("#excludeKeywordUuid").val(data.uuid);
                $("#excludeCustomerKeywordDialog").find("#customerKeywordDialogContent").val(data.keyword);
            }
        },
        error: function () {
            $().toastmessage('showErrorToast', "显示已添加排除词失败�);
        }
    });
}
