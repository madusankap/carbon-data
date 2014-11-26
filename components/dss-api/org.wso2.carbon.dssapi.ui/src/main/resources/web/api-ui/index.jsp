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

<%
    APIPublisherClient apiPublisherClient=null;
    int numServices = 0;
    ServiceMetaData[] serviceList=null;

    try {
        String backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
        ConfigurationContext configContext =
                (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
        String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);

        apiPublisherClient = new APIPublisherClient(cookie,backendServerURL,configContext);
        serviceList = apiPublisherClient.getServices();
        numServices = serviceList.length;
    } catch (Exception e) {
        CarbonError carbonError = new CarbonError();
        carbonError.addError("Error occurred while saving data service configuration.");
        request.setAttribute(CarbonError.ID, carbonError);
        String errorMsg = e.getMessage();
%>

<script type="text/javascript">
    location.href = "error.jsp?errorMsg=<%=errorMsg%>";
</script>
<%
    }
%>

<div id="middle">
    <h2>API Publisher Console</h2>
    <div id="workArea">
        <label><%=numServices%> active service(s)</label>
        <br />
        <table class="styledLeft" id="moduleTable">
            <thead>
                <tr>
                    <th width="30%">Service Name</th>
                    <th width="30%">Description</th>
                    <th width="30%">API Availability</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if(serviceList!=null) {
                        for(int i=0;i<numServices;i++) {
                %>
                <tr>
                    <td width="30%"><% serviceList[i].getName(); %></td>
                    <td width="30%"><% serviceList[i].getDescription(); %></td>
                    <td width="30%"><% apiPublisherClient.isAPIAvailable(serviceList[i]); %></td>
                </tr>
                <%
                        }
                    }
                    else {
                        //put an error message
                    }
                %>
            </tbody>
        </table>
    </div>
</div>