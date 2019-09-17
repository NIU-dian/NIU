<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/5/21
  Time: 10:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>交易日K数据补漏</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

</head>
<body>
<div class="modal fade" id="loadingModal" backdrop="static" keyboard="false">
    　　<div style="width: 200px;height:20px; z-index: 20000; position: absolute; text-align: center; left: 50%; top: 50%;margin-left:-100px;margin-top:-10px">
    　　　　<h5 id="loadText"><img src="static/img/loading.gif"></h5>
    　　</div>
</div>
<div align="center">
    <table align="center">
        <tr>
            <td colspan="2" align="center"><span style="font-size: 22px; font-weight: bold;">K线数据修补</span></td>
        </tr>
        <tr>
            <td>股票市场：</td>
            <td>
                <select id="market" name="market">
                    <option value="SH">上证</option>
                    <option value="SZ">深证</option>
                    <option value="HKSE">港股</option>
                    <option value="AMEX">美交所</option>
                    <option value="NYSE">纽交所</option>
                    <option value="NASDAQ">纳斯达克</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>股票代码：</td>
            <td>
                <input type="text" name="symbol" id="symbol"/>
            </td>
        </tr>
        <tr>
            <td>开始时间：</td>
            <td>
                <input type="text" name="begin" id="begin" placeholder="格式：yyyyMMddHHmm"/>
            </td>
        </tr>
        <tr>
            <td>K线类型：</td>
            <td>
                <select id="period" name="period">
                    <option value="day" selected="selected">日K</option>
                    <option value="1m">一分钟</option>
                </select>
            </td>
        </tr>
        <tr>
            <td><input type="button" id="send" onclick="postRequest()" value="提交"/></td>
            <td><input type="button" id="reset" onclick="reset()" value="重置"/></td>
        </tr>
    </table>
<p>==============================================================================================================================</p>
    <table align="center" style="overflow: auto">
        <tr>
            <td colspan="2" align="center"><span style="font-size: 22px; font-weight: bold;">分时数据修补</span></td>
        </tr>
        <tr>
            <td>股票市场：</td>
            <td>
                <select id="m_market" name="m_market">
                    <option value="SH">上证</option>
                    <option value="SZ">深证</option>
                    <option value="HKSE">港股</option>
                    <option value="AMEX">美交所</option>
                    <option value="NYSE">纽交所</option>
                    <option value="NASDAQ">纳斯达克</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>股票代码：</td>
            <td>
                <input type="text" name="m_symbol" id="m_symbol" style="width: 230px;" placeholder="上证、深证请添加前缀SZ、SH"/>
            </td>
        </tr>
        <tr>
            <td>K线类型：</td>
            <td>
                <select id="m_period" name="m_period">
                    <option value="5d" selected="selected">5日</option>
                    <option value="1d">分钟</option>
                </select>
            </td>
        </tr>
        <tr>
            <td><input type="button" id="m_send" onclick="postMinuteRequest()" value="提交"/></td>
            <td><input type="button" id="m_reset" onclick="reset()" value="重置"/></td>
        </tr>
        <tr>
            <td align="center" colspan="2">

            </td>
        </tr>
    </table>

    <div>
        <p id="result"></p>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script>
    function postRequest() {
        var market = $("#market").val();
        var symbol = $("#symbol").val();
        var begin = $("#begin").val();
        var period = $("#period").val();
        if( isEmpty(market) ){
            alert("市场不能为空！");
            return;
        }
        if( isEmpty(symbol) ){
            alert("股票代码不能为空！");
            return;
        }
        if( isEmpty(begin) ){
            alert("开始时间不能为空！");
            return;
        }
        if( isEmpty(period) ){
            alert("K线类型不能为空！");
            return;
        }
        $('#loadingModal').modal('show');

        $.post(
            "xueqiu/getDailyData",
            {
                market: market,
                symbol: symbol,
                begin: begin,
                period: period
            },
            function (resultJSONObject) {
                $("#result").html(JSON.stringify(resultJSONObject));
                $('#loadingModal').modal('hide');
            }, "json");
    }

    function postMinuteRequest() {
        var market = $("#m_market").val();
        var symbol = $("#m_symbol").val();
        var period = $("#m_period").val();
        if( isEmpty(market) ){
            alert("市场不能为空！");
            return;
        }
        if( isEmpty(symbol) ){
            alert("股票代码不能为空！");
            return;
        }
        if( isEmpty(period) ){
            alert("K线类型不能为空！");
            return;
        }
        $('#loadingModal').modal('show');

        $.post(
            "xueqiu/getMinuteData",
            {
                market: market,
                symbol: symbol,
                period: period
            },
            function (resultJSONObject) {
                $("#result").html(JSON.stringify(resultJSONObject));
                $('#loadingModal').modal('hide');
            }, "json");
    }

    function isEmpty(param){
        if( param == '' || param == undefined){
            return true;
        }
        return false;
    }



    var websocket;
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:8080/jin10");
    } else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket("ws://localhost:8080/hcm/webSocket/jin10");
    } else {
        websocket = new SockJS("http://localhost:8080/hcm/webSocket/sockjs/jin10");
    }
    websocket.onopen = function (evnt) {
        console.log("webSocekt connected")
    };
    websocket.onmessage = function (evnt) {
        console.log(evnt.data);
    };
    websocket.onerror = function (evnt) {
        console.log(evnt)
    };
    websocket.onclose = function (evnt) {
        console.log("webSocket close")
    }
</script>
</body>
</html>
