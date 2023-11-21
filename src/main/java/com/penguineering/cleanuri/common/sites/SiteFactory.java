package com.penguineering.cleanuri.common.sites;

import com.penguineering.cleanuri.site.Site;
import com.penguineering.cleanuri.site.SiteLoader;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Factory class responsible for providing site implementations discovered on the classpath.
 * <p>
 * This class uses the {@link SiteLoader} to find and load all available implementations of the {@link Site} interface.
 * The loaded sites are then logged and returned as a list.
 */
@Factory
public class SiteFactory {
    private final SiteLoader siteModuleLoader;
    private static final Logger logger = LoggerFactory.getLogger(SiteFactory.class);

    @Inject
    public SiteFactory(SiteLoader siteModuleLoader) {
        this.siteModuleLoader = siteModuleLoader;
    }

    @Singleton
    public List<Site> sites() {
        List<Site> sites = siteModuleLoader.findSitesOnClasspath();
        sites.forEach(site -> logger.info("Discovered site: " + site.getSiteDescriptor().getLabel()));
        return sites;
    }
}