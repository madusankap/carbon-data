/*
 *  Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.dssapi.util;

import org.apache.axis2.Constants;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.APIProvider;
import org.wso2.carbon.apimgt.api.model.API;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.api.model.APIStatus;
import org.wso2.carbon.apimgt.api.model.URITemplate;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.impl.internal.ServiceReferenceHolder;

import java.util.LinkedHashSet;
import java.util.Set;

public class APIUtil {
    private static final String HTTP_PORT = "mgt.transport.http.port";
    private static final String HOST_NAME = "carbon.local.ip";

    private APIProvider getAPIProvider(String username) {
        try {
            return APIManagerFactory.getInstance().getAPIProvider(username);
        } catch (APIManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isAPIProviderReady() {
        return ServiceReferenceHolder.getInstance()
                .getAPIManagerConfigurationService() != null;
    }

    public void addApi(String ServiceId, String username, String tenantName) {
        if (isAPIProviderReady()) {
            String providerName;
            String apiEndpoint;
            String apiContext;
            APIProvider apiProvider;
            if ("carbon.super".equals(tenantName)) {
                providerName = username;
                apiEndpoint = "http://" + System.getProperty(HOST_NAME) + ":" + System.getProperty(HTTP_PORT) + "/services/" + ServiceId + "?wsdl";
                apiContext = "/services/" + ServiceId;
                apiProvider = getAPIProvider(providerName);
            } else {
                providerName = username + "-AT-" + tenantName;
                apiEndpoint = "http://" + System.getProperty(HOST_NAME) + ":" + System.getProperty(HTTP_PORT) + "/services/t/" + tenantName + "/" + ServiceId + "?wsdl";
                apiContext = "/services/t/" + tenantName + "/" + ServiceId;
                apiProvider = getAPIProvider(username + "@" + tenantName);
            }

            String provider = providerName; //todo get correct provider(username) for tenants
            String apiVersion = "1.0.0";
            String apiName = ServiceId;
            String iconPath;
            String documentURL;
            String authType = "Any";

            APIIdentifier identifier = new APIIdentifier(provider, apiName, apiVersion);
            try {
                if (apiProvider.isAPIAvailable(identifier)) {
                    return;
                }
            } catch (APIManagementException e) {
                e.printStackTrace();
            }
            API api = createAPIModel(apiProvider, apiContext, apiEndpoint, authType, identifier);

            if (api != null) {
                try {
                    apiProvider.addAPI(api);
                } catch (APIManagementException e) {
                    e.printStackTrace();
                }
            }
        } else {

        }

    }

    private API createAPIModel(APIProvider apiProvider, String apiContext, String apiEndpoint, String authType, APIIdentifier identifier) {
        API api = null;
        try {
            api = new API(identifier);
            api.setContext(apiContext);
            api.setUriTemplates(getURITemplates(apiEndpoint, authType));
            api.setVisibility(APIConstants.API_GLOBAL_VISIBILITY);
            api.addAvailableTiers(apiProvider.getTiers());
            api.setEndpointSecured(false);
            api.setStatus(APIStatus.PUBLISHED);
            api.setTransports(Constants.TRANSPORT_HTTP + "," + Constants.TRANSPORT_HTTPS);
            api.setSubscriptionAvailability(APIConstants.SUBSCRIPTION_TO_ALL_TENANTS);
            api.setResponseCache(APIConstants.DISABLED);
            api.setImplementation("endpoint");
            String endpointConfig = "{\"production_endpoints\":{\"url\":\"" + apiEndpoint + "\",\"config\":null},\"wsdlendpointService\":\"" + identifier.getApiName() + "\",\"wsdlendpointPort\":\"SOAP12Endpoint\",\"endpoint_type\":\"wsdl\"}";
            api.setEndpointConfig(endpointConfig);
            api.setWsdlUrl(apiEndpoint);
        } catch (APIManagementException e) {
            e.printStackTrace();
        }
        return api;
    }

    private Set<URITemplate> getURITemplates(String endpoint, String authType) {
        //todo improve to add sub context paths for uri templates as well
        Set<URITemplate> uriTemplates = new LinkedHashSet<URITemplate>();
        String[] httpVerbs = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};

        if (authType.equals(APIConstants.AUTH_NO_AUTHENTICATION)) {
            for (int i = 0; i < 5; i++) {
                URITemplate template = new URITemplate();
                template.setAuthType(APIConstants.AUTH_NO_AUTHENTICATION);
                template.setHTTPVerb(httpVerbs[i]);
                template.setResourceURI(endpoint);
                template.setUriTemplate("/*");
                uriTemplates.add(template);
            }
        } else {
            for (int i = 0; i < 5; i++) {
                URITemplate template = new URITemplate();
                if (i != 4) {
                    template.setAuthType(APIConstants.AUTH_APPLICATION_OR_USER_LEVEL_TOKEN);
                } else {
                    template.setAuthType(APIConstants.AUTH_NO_AUTHENTICATION);
                }
                template.setHTTPVerb(httpVerbs[i]);
                template.setResourceURI(endpoint);
                template.setUriTemplate("/*");
                uriTemplates.add(template);
            }
        }

        return uriTemplates;
    }

    public boolean apiAvailable(String ServiceId, String username, String tenantDomain) {
        boolean apiAvailable = false;
        if (isAPIProviderReady()) {
            //String username= CarbonContext.getThreadLocalCarbonContext().getUsername();

            APIProvider apiProvider;
            String provider;
            if ("carbon.super".equals(tenantDomain)) {
                provider = username;
                apiProvider = getAPIProvider(username);
            } else {
                provider = username + "-AT-" + tenantDomain;
                apiProvider = getAPIProvider(username + "@" + tenantDomain);
            }
            String apiVersion = "1.0.0";
            String apiName = ServiceId;
            APIIdentifier identifier = new APIIdentifier(provider, apiName, apiVersion);
            try {
                apiAvailable = apiProvider.isAPIAvailable(identifier);
            } catch (APIManagementException e) {
                e.printStackTrace();
            }
        }
        return apiAvailable;
    }

    public void removeApi(String ServiceId, String username, String tenantDomain) {
        if (isAPIProviderReady()) {
            APIProvider apiProvider;
            String provider;
            if ("carbon.super".equals(tenantDomain)) {
                provider = username;
                apiProvider = getAPIProvider(username);
            } else {
                provider = username + "-AT-" + tenantDomain;
                apiProvider = getAPIProvider(username + "@" + tenantDomain);
            }
            String apiVersion = "1.0.0";

            String apiName = ServiceId;

            APIIdentifier identifier = new APIIdentifier(provider, apiName, apiVersion);
            try {
                if (apiProvider.isAPIAvailable(identifier)) {
                    apiProvider.deleteAPI(identifier);
                }
            } catch (APIManagementException e) {
                e.printStackTrace();
            }
        }
    }
}

