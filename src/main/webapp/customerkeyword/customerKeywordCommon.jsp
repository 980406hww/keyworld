<%--Dialog部分--%>
<div id="saveCustomerKeywordDialog" class="easyui-dialog" style="display: none;left: 35%;">
    <form id="customerKeywordForm">
        <ul id="tcontent">
            <input type="hidden" id="uuid" value="" style="width:300px;">
            <input type="hidden" id="optimizedCount" value="" style="width:300px;">
            <div id="KeywordDiv">
                <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">关键字:</span><input type="text" name="keyword" id="keyword" value="" style="width:300px;"/></li>
                <hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;"/>
            </div>
            <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">标题:</span><input type="text" name="title" id="title" value="" style="width:300px;"></li>
            <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">域名:</span><input type="text" name="url" id="url" value="" style="width:300px;"></li>
            <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">熊掌号:</span><input type="text" name="bearPawNumber" id="bearPawNumber" value="" style="width:300px;"></li>
            <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">原始域名:</span><input type="text" name="originalUrl" id="originalUrl" value="" style="width:300px;"></li>
            <li onclick="checkItem(this)"><span class="customerKeywordSpanClass" >优化组名:</span><input name="optimizeGroupName" id="optimizeGroupName" type="text" style="width:200px;" value=""></li>
            <li onclick="checkItem(this)"><span class="customerKeywordSpanClass" >机器分组:</span><input name="machineGroup" id="machineGroup" type="text" style="width:200px;" value="Default"></li>
            <li onclick="checkItem(this)"><span class="customerKeywordSpanClass" >目标城市:</span><input name="city" id="city" type="text" style="width:200px;"></li>
            <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">要刷数量:</span><input type="text" name="optimizePlanCount" id="optimizePlanCount" value="" size="5" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)"></li>
            <li onclick="checkItem(this)"><span class="customerKeywordSpanClass" >指数:</span><input type="text" id="initialIndexCount" size="5" name="initialIndexCount" value="100" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)"></li>
            <li onclick="checkItem(this)"><span class="customerKeywordSpanClass" >排名:</span><input type="text" id="initialPosition" size="5" name="initialPosition" value="10" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)" ></li>
            <li onclick="checkItem(this)"><span style="margin-left: 3px">关键词作用:</span>
                <select name="keywordEffect" id="keywordEffect" style="width: 100px" title="">
                    <c:forEach items="${keywordEffects}" var="keywordEffect">
                        <option value="${keywordEffect}" <c:if test="${keywordEffect=='Common'}">selected="selected"</c:if>>
                            <c:if test="${keywordEffect=='Common'}">一般词</c:if>
                            <c:if test="${keywordEffect=='Curve'}">曲线词</c:if>
                            <c:if test="${keywordEffect=='Appointment'}">指定词</c:if>
                            <c:if test="${keywordEffect=='Present'}">赠送词</c:if>
                        </option>
                    </c:forEach>
                </select>
            </li>

            <li onclick="checkItem(this)">
                <ul style="float: left">
                    <li onclick="checkItem(this)" >
                        <span class="customerKeywordSpanClass">第一报价:</span><input name="positionFirstFee" id="positionFirstFee" value=""
                                                                                  style="width:100px;"
                                                                                  type="text" onkeyup="onlyNumber(this)" onblur="setSecondThirdDefaultFee(this)">元 </li>
                    <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">第二报价:</span><input name="positionSecondFee"
                                                                                  id="positionSecondFee" value="" style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                    </li>
                    <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">第三报价:</span><input name="positionThirdFee" id="positionThirdFee" value=""
                                                                                  style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="setThirdDefaultFee(this)">元
                    </li>
                    <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">第四报价:</span><input name="positionForthFee" id="positionForthFee" value=""
                                                                                  style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="setForthDefaultFee(this)">元
                    </li>
                    <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">第五报价:</span><input name="positionFifthFee" id="positionFifthFee" value=""
                                                                                  style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                    </li>
                    <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">首页报价:</span><input name="positionFirstPageFee" id="positionFirstPageFee" value=""
                                                                                  style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                    </li></ul>


                    <ul id="customerKeywordCost" style="float: left; width: 200px;height:146px;text-align: center">
                        <li onclick="checkItem(this)"><a href="javascript:showCustomerKeywordCost()">&nbsp;显示成本(再次点击关闭)</a></li>
                        <ul id="customerKeywordCostFrom" style="display: none;">
                            <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">第一成本:</span><input name="positionFirstCost" id="positionFirstCost"
                                                                                          onBlur="setSecondThirdDefaultCost();" value=""
                                                                                          style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元 </li>
                            <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">第二成本:</span><input name="positionSecondCost" id="positionSecondCost"
                                                                                          onBlur="setThirdDefaultCost();" value=""
                                                                                          style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                            </li>
                            <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">第三成本:</span><input name="positionThirdCost" id="positionThirdCost"
                                                                                          value="" style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                            </li>
                            <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">第四成本:</span><input name="positionForthCost" id="positionForthCost"
                                                                                          value="" style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                            </li>
                            <li onclick="checkItem(this)" ><span class="customerKeywordSpanClass">第五成本:</span><input name="positionFifthCost" id="positionFifthCost"
                                                                                          value="" style="width:100px;" type="text" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)">元
                            </li>
                        </ul>
                    </ul>
            </li>
            <li onclick="checkItem(this)" style="float:left" ><span class="customerKeywordSpanClass">服务提供商:</span>
                <select name="serviceProvider" id="serviceProvider" style="width: 296px">
                    <c:forEach items="${serviceProviders}" var="serviceProvider">
                        <option value="${serviceProvider.serviceProviderName}" <c:if test="${serviceProvider.serviceProviderName=='baidutop123'}">selected="selected"</c:if>>${serviceProvider.serviceProviderName}</option>
                    </c:forEach>
                </select></li>
            <li onclick="checkItem(this)"><span class="customerKeywordSpanClass" >订单号:</span><input name="orderNumber" id="orderNumber" type="text" style="width:300px;"/></li>
            <hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;"/>
            <li style="float:left" ><span class="customerKeywordSpanClassa" onclick="checkItem(this)">收费方式:
                <select name="collectMethod" id="collectMethod" onChange="setEffectiveToTime()">
                    <option value="PerMonth" selected>按月</option>
                    <option value="PerTenDay">十天</option>
                    <option value="PerSevenDay">七天</option>
                    <option value="PerDay">按天</option>
                </select>
               </span>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <span class="customerKeywordSpanClassa" onclick="checkItem(this)">搜索引擎:
                <select name="searchEngine" id="searchEngine" >
                    <option value="百度">百度</option>
                    <c:forEach items="${searchEngineMap}" var="entry">
                        <option value="${entry.value}">${entry.key}</option>
                    </c:forEach>
                </select>
                </span>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="hidden" id="status" name="status" value="1">
                <span class="customerKeywordSpanClassa" onclick="checkItem(this)">排序:
                    <input type="text" name="sequence" id="sequence" value="0" size="6" onkeyup="onlyNumber(this)" onblur="onlyNumber(this)"></span>
            </li>

            <hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;"/>
            <li style="float:left">
                <span class="customerKeywordSpanClassa" onclick="checkItem(this)">收费状态:<select name="paymentStatus" id="paymentStatus">
                <option value="0"></option>
                <option value="1">担保中</option>
                <option value="2">已付费</option>
                <option value="3">未担保</option>
                <option value="4">跑路</option>
            </select>
                    </span>
            </li>
            <li style="float:left;"  >
                <span style="text-align: right;margin-left: 30px;" class="customerKeywordSpanClassa"  onclick="checkItem(this)" >
                <input type="radio" id="clickPositiveUrl" name="clickUrl" value="clickPositiveUrl">点击正面链接
                </span>
                <span style="text-align: right;margin-left: 30px; " class="customerKeywordSpanClassa" onclick="checkItem(this)" >
                <input type="radio" id="clickCommonUrl" name="clickUrl" value="clickCommonUrl">点击非负面清单链接
                </span>
            </li>
            <li style="float:left"  >
                <span style="text-align: right;margin-left: 30px;"  onclick="checkItem(this)" class="customerKeywordSpanClassa">
                <input type="checkbox" id="operateSelectKeyword" name="operateSelectKeyword" value="operateSelectKeyword" >下拉词
                </span>
                <span style="text-align: right;margin-left: 30px;"  onclick="checkItem(this)" class="customerKeywordSpanClassa">
                <input type="checkbox" id="operateRelatedKeyword" name="operateRelatedKeyword" value="operateRelatedKeyword"  >相关词
                </span>
                <span style="text-align: right;margin-left: 30px;"  onclick="checkItem(this)" class="customerKeywordSpanClassa">
                    <input type="checkbox" id="operateRecommendKeyword" name="operateRecommendKeyword" value="operateRecommendKeyword"  >推荐词</span>
                <span style="text-align: right;margin-left: 30px;"  onclick="checkItem(this)" class="customerKeywordSpanClassa">
                <input type="checkbox" id="operateSearchAfterSelectKeyword" name="operateSearchAfterSelectKeyword" value="operateSearchAfterSelectKeyword"  >搜索后下拉词
                </span>
            </li>
            <li onclick="checkItem(this)" style="float:left"  ><span class="customerKeywordSpanClass">推荐词:</span><input type="text" name="recommendKeywords" id="recommendKeywords" value="" style="width:300px;"></li>
            <li onclick="checkItem(this)" style="float:left"  ><span class="customerKeywordSpanClass">负面词:</span><input type="text" name="negativeKeywords" id="negativeKeywords" value="" style="width:300px;"></li>
            <li onclick="checkItem(this)" style="float:left"  ><span class="customerKeywordSpanClass">排除词:</span><input type="text" name="excludeKeywords" id="excludeKeywords" value="" style="width:300px;"></li>
            <li onclick="checkItem(this)" style="float:left"  ><span class="customerKeywordSpanClass">展现页码:</span><input type="text" name="showPage" id="showPage" value="" placeholder="2,3,4,5" style="width:300px;"></li>
            <li onclick="checkItem(this)" style="float:left"  ><span style="text-align: right;margin-left: 5px;">普通相关词占比:</span>
                <input type="text" name="relatedKeywordPercentage" id="relatedKeywordPercentage" class="easyui-numberspinner" data-options="min:0,max:100,increment:10" style="width:260px;">%
            </li>
            <li onclick="checkItem(this)" style="float:left"  >
                <span class="customerKeywordSpanClass" style="display: inline-block;float: left;height: 80px;">备注:</span><textarea name="remarks" id="remarks" style="width:300px;height:80px;resize: none" placeholder="请写备注吧!"></textarea>
            </li>
        </ul>
    </form>
</div>

<%--Dialog部分--%>
<div id="targetBearPawNumberDialog" style="text-align: center;left: 40%;display: none;">
    <form id="bearPawNumberChangeForm" style="text-align: center;margin-top: 10px;" onkeydown="if(event.keyCode==13)return false;">
        熊掌号:<input type="text" id="targetBearPawNumber" name="targetBearPawNumber" style="width:150px;"><%-- margin-top: 10px;--%>
    </form>
</div>


<%--Dialog部分--%>
<div id="targetMachineGroupDialog" style="text-align: center;left: 40%;display: none;">
    <form id="targetMachineGroupForm" style="text-align: center;margin-top: 10px;">
        目标机器分组名:<input type="text" id="machineGroup" name="machineGroup" style="width:150px">
    </form>
</div>
