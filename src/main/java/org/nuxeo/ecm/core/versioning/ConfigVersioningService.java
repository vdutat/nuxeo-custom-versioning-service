package org.nuxeo.ecm.core.versioning;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.VersioningOption;
import org.nuxeo.ecm.core.model.Document;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.services.config.ConfigurationService;

public class ConfigVersioningService extends StandardVersioningService implements VersioningService {
	
	public static final String AUTOMATION_CHAIN_PROP_PATTERN = "nuxeo.versioning.versioningservice.%s.from.%s.followTransition";

    private static final Log LOGGER = LogFactory.getLog(ConfigVersioningService.class);

    public static final String DEFAULT_LIFECYCLE_POLICY = "default";

	@Override
	protected void followTransitionByOption(Document doc, VersioningOption option) {
		String transition = getTransition(doc.getLifeCyclePolicy(), doc.getLifeCycleState());
		if (transition != null) {
			LOGGER.debug("Following transition '" + transition + "'");
			doc.followTransition(transition);
		} else if (DEFAULT_LIFECYCLE_POLICY.equals(doc.getLifeCyclePolicy())) {
			LOGGER.debug("Calling " + DEFAULT_LIFECYCLE_POLICY + " 'followTransitionByOption' implementation");
			super.followTransitionByOption(doc, option);
		} else {
			LOGGER.debug("No transition followed");
		}
	}

	protected String getTransition(String lifecyclePolicy, String lifecycleState) {
        ConfigurationService cs = Framework.getService(ConfigurationService.class);
        return cs.getProperty(String.format(AUTOMATION_CHAIN_PROP_PATTERN, lifecyclePolicy, lifecycleState));
	}

}
