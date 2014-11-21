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

/**
 * Created by tharindud on 11/20/14.
 */
public class APIUtil {
    private static final String httpPort = "mgt.transport.http.port";
    private static final String hostName = "carbon.local.ip";

    private APIProvider getAPIProvider() {
        try {
            //todo get current user
            return APIManagerFactory.getInstance().getAPIProvider("admin");
        } catch (APIManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isAPIProviderReady() {
        if (ServiceReferenceHolder.getInstance()
                .getAPIManagerConfigurationService() != null) {
            return true;
        }
        return false;
    }
    public void addApi(String ServiceId) {

            if (isAPIProviderReady()) {
                //todo check null
                // if null --> add to map
                APIProvider apiProvider = getAPIProvider();

                String provider = "admin"; //todo get correct provider(username) for tenants

                String apiVersion ="1.0.0";

                String apiName = ServiceId;


                String apiEndpoint = "http://" + System.getProperty(hostName) + ":" + System.getProperty(httpPort) +"/services/"+ apiName;

                String iconPath = "";
                String documentURL = "";
                String authType = "Any";

                APIIdentifier identifier = new APIIdentifier(provider, apiName, apiVersion);
                try {
                    if (apiProvider.isAPIAvailable(identifier)) {
                        //todo : do nothing ?? update API ?
//                        apiProvider.deleteAPI(identifier);
                        return;
                    }
                } catch (APIManagementException e) {
e.printStackTrace();
                }

                API api = createAPIModel(apiProvider, apiName, apiEndpoint, authType, identifier);

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
            api.setUrl(apiEndpoint);

            api.setUriTemplates(getURITemplates(apiEndpoint, authType));
            api.setVisibility(APIConstants.API_GLOBAL_VISIBILITY);
            api.addAvailableTiers(apiProvider.getTiers());
            api.setEndpointSecured(false);
            api.setStatus(APIStatus.PUBLISHED);
            api.setTransports(Constants.TRANSPORT_HTTP + "," + Constants.TRANSPORT_HTTPS);
            api.setSubscriptionAvailability(APIConstants.SUBSCRIPTION_TO_ALL_TENANTS);
            api.setResponseCache(APIConstants.DISABLED);
            String endpointConfig = "{\"production_endpoints\":{\"url\":\" "+ apiEndpoint + "\",\"config\":null},\"endpoint_type\":\"http\"}";
            api.setEndpointConfig(endpointConfig);

        } catch (APIManagementException e) {
e.printStackTrace();
        }
        return api;
    }
    private Set<URITemplate> getURITemplates(String endpoint, String authType) {
        //todo improve to add sub context paths for uri templates as well
        Set<URITemplate> uriTemplates = new LinkedHashSet<URITemplate>();
        String[] httpVerbs = { "GET", "POST", "PUT", "DELETE", "OPTIONS" };

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
    }

