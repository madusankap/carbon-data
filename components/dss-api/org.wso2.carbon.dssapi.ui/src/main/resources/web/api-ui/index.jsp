<%@ page import="org.wso2.carbon.dssapi.common.APIModel" %>
<%@ page import="org.wso2.carbon.dssapi.stub.*" %>
<%@ page import="org.wso2.carbon.dssapi.core.APIPublisher" %>
<%@ page import="org.wso2.carbon.service.mgt.xsd.ServiceMetaDataWrapper" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" prefix="carbon" %>

<script type="text/javascript">
    location.href = "../admin/error.jsp";
</script>

<%
    APIPublisherStub apiPublisherStub = new APIPublisherStub();
    ServiceMetaDataWrapper serviceMetaDataWrapper;
    serviceMetaDataWrapper = apiPublisherStub.listDssServices("", 0);

    int activeServices = serviceMetaDataWrapper.getNumberOfActiveServices();
%>

<div id="middle">
    <h2>API Publisher Console</h2>
    <div id="workArea">
        <label>Number of active services : <%=activeServices%></label>
        <table class="styledLeft" id="moduleTable">
            <thead>
                <tr>
                    <th width="30%">Item Id</th>
                    <th width="30%">Item Name</th>
                    <th width="30%">Item Price</th>
                </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
    </div>
</div>