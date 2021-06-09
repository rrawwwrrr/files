package ru.lanit.rnd.files.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.ldap.core.support.BaseLdapPathContextSource
import org.springframework.ldap.core.support.LdapContextSource
import org.springframework.security.authentication.*
import org.springframework.security.core.Authentication
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class LdapService(private val jwtSigner: JwtSigner) {
    @Value("\${spring.ldap.urls}")
    private val ldapUrl: String? = null

    @Value("\${spring.ldap.domain}")
    private val ldapDomain: String? = null

    //    @Bean
    fun authenticationManager(): ReactiveAuthenticationManager {
        val adlap = ActiveDirectoryLdapAuthenticationProvider(ldapDomain, "ldap://$ldapUrl")
        val am: AuthenticationManager = ProviderManager(listOf(adlap))
        return ReactiveAuthenticationManagerAdapter(am)
    }

    fun auth(auth: Authentication): Mono<String> {
        return jwtSigner.createJwt(authenticationManager().authenticate(auth));
    }

    //    @Bean
    fun contextSource(): BaseLdapPathContextSource? {
        val ctx = LdapContextSource()
        ctx.setUrl(ldapUrl)
        ctx.afterPropertiesSet()
        return ctx
    }
}