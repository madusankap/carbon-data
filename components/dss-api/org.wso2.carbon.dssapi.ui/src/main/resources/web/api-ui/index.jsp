<%@ page import="org.wso2.carbon.dssapi.stub.*" %>
<%@ page import="org.wso2.carbon.dssapi.core.*" %>
<%@ page import="org.wso2.carbon.service.mgt.xsd.*" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.dssapi.ui.APIPublisherClient" %>
<%@ page import="org.wso2.carbon.CarbonError" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" prefix="carbon" %>

<script type="text/javascript">
    location.href = "../admin/error.jsp";
</script>

<%
    int activeServices = 0;
    try {
        String backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
        ConfigurationContext configContext =
                (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
        String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);

        APIPublisherClient apiPublisherClient = new APIPublisherClient(cookie,backendServerURL,configContext);
        activeServices = apiPublisherClient.getNumberOfActiveServices();
    } catch (Exception e) {
        CarbonError carbonError = new CarbonError();
        carbonError.addError("Error occurred while saving data service configuration.");
        request.setAttribute(CarbonError.ID, carbonError);
        String errorMsg = e.getLocalizedMessage();
%>
<script type="text/javascript">
    location.href = "../ds/dsErrorPage.jsp?errorMsg=<%=errorMsg%>";
</script>
<%
    }
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