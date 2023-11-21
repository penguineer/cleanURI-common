package com.penguineering.cleanuri.common.sites;

import com.penguineering.cleanuri.site.SiteLoader;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

/**
 * Factory for creating a singleton {@link SiteLoader} bean.
 * This class is necessary as {@link SiteLoader} is independent of the Micronaut injection framework.
 * By defining {@link SiteLoader} as a singleton bean, we ensure one instance is shared across the application.
 */
@Factory
public class SiteLoaderBean {

    /**
     * Provides a singleton {@link SiteLoader} bean.
     * This method creates a new {@link SiteLoader} instance, managed as a singleton bean by the application context.
     *
     * @return a new {@link SiteLoader} instance
     */
    @Bean
    @Singleton
    public SiteLoader siteModuleLoader() {
        return new SiteLoader();
    }
}