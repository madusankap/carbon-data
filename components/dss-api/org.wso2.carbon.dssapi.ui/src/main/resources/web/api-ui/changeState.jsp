<!--
~ Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
~
~ WSO2 Inc. licenses this file to you under the Apache License,
~ Version 2.0 (the "License"); you may not use this file except
~ in compliance with the License.
~ You may obtain a copy of the License at
~
~    http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing,
~ software distributed under the License is distributed on an
~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
~ KIND, either express or implied.  See the License for the
~ specific language governing permissions and limitations
~ under the License.
-->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="org.wso2.carbon.dssapi.ui.APIPublisherClient" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ page import="org.wso2.carbon.ui.util.CharacterEncoder" %>

<fmt:bundle basename="org.wso2.carbon.dssapi.ui.i18n.Resources">
    <%
        String serviceName =  CharacterEncoder.getSafeText(request.getParameter("serviceName"));
        String isAvailable =  CharacterEncoder.getSafeText(request.getParameter("isAvailable"));
        if (serviceName == null || serviceName.trim().length() == 0) {
    %>
    <p><fmt:message key="service.name.cannot.be.null"/></p>


    <%
            return;
        }
        String backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
        ConfigurationContext configContext =
                (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);

        String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
        APIPublisherClient client;
        try {
            client = new APIPublisherClient(cookie,backendServerURL,configContext);
            Boolean isAvailableBool = Boolean.valueOf(isAvailable);
            if(!isAvailableBool)
                client.unpublishAPI(client.getServiceData(serviceName).getServices()[0]);
            else
                client.publishAPI(client.getServiceData(serviceName).getServices()[0]);
            request.setAttribute("serviceName", serviceName);
            request.setAttribute("isAvailable", isAvailable);

        }
        catch (Exception e) {
            CarbonUIMessage uiMsg = new CarbonUIMessage(CarbonUIMessage.ERROR, e.getMessage(), e);
            session.setAttribute(CarbonUIMessage.ID, uiMsg);
    %>
    <jsp:include page="error.jsp"/>
    <%
            return;
        }

    %>

    <jsp:include page="publishState.jsp" />
</fmt:bundle>