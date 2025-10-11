package com.example.security.server.persistence.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "security_saml2_relying_party",
        uniqueConstraints = {@UniqueConstraint(name = "uk_rp_ap_meta_id", columnNames = {"asserting_party_metadata_id"})})
public class SamlRelyingPartyEntity {
    @Id
    @Column(name = "registration_id")
    private String registrationId;
    @Column(name = "entity_id")
    private String entityId;
    @Column(name = "assertion_consumer_service_location")
    private String assertionConsumerServiceLocation;
    @Column(name = "assertion_consumer_service_binding", length = 10)
    private String assertionConsumerServiceBinding;
    @Column(name = "single_logout_service_location")
    private String singleLogoutServiceLocation;
    @Column(name = "single_logout_service_response_location")
    private String singleLogoutServiceResponseLocation;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "security_saml2_single_logout_service_bindings",
            catalog = "authentication_server",
            joinColumns = @JoinColumn(name = "registration_id", foreignKey = @ForeignKey(name = "fk_rp_sb_reg_id"))
    )
    private Set<String> singleLogoutServiceBindings;
    @Column(name = "name_id_format")
    private String nameIdFormat;
    @Column(name = "authn_requests_signed")
    private boolean authnRequestsSigned;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "asserting_party_metadata_id", foreignKey = @ForeignKey(name = "fk_rp_ap_meta_id"))
    private Saml2AssertingPartyEntity assertingPartyMetadata;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable
            (
                    name = "security_saml2_relying_party_decryption_credentials",
                    catalog = "authentication_server",
                    joinColumns = {@JoinColumn(name = "registration_id", foreignKey = @ForeignKey(name = "fk_rp_dc_reg_id"), referencedColumnName = "registration_id")},
                    inverseJoinColumns = {@JoinColumn(name = "credentials_id", foreignKey = @ForeignKey(name = "fk_rp_dc_cred_id"), referencedColumnName = "id", unique = true)},
                    uniqueConstraints = {@UniqueConstraint(name = "uk_rp_dc_cred_id", columnNames = {"credentials_id"})}
            )
    private Set<SamlCredentialEntity> decryptionX509Credentials;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable
            (
                    name = "security_saml2_relying_party_signing_credentials",
                    catalog = "authentication_server",
                    joinColumns = {@JoinColumn(name = "registration_id", foreignKey = @ForeignKey(name = "fk_rp_sc_reg_id"), referencedColumnName = "registration_id")},
                    inverseJoinColumns = {@JoinColumn(name = "credentials_id", foreignKey = @ForeignKey(name = "fk_rp_sc_cred_id"), referencedColumnName = "id", unique = true)},
                    uniqueConstraints = {@UniqueConstraint(name = "uk_rp_sc_cred_id", columnNames = {"credentials_id"})}
            )
    private Set<SamlCredentialEntity> signingX509Credentials;

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getAssertionConsumerServiceLocation() {
        return assertionConsumerServiceLocation;
    }

    public void setAssertionConsumerServiceLocation(String assertionConsumerServiceLocation) {
        this.assertionConsumerServiceLocation = assertionConsumerServiceLocation;
    }

    public String getAssertionConsumerServiceBinding() {
        return assertionConsumerServiceBinding;
    }

    public void setAssertionConsumerServiceBinding(String assertionConsumerServiceBinding) {
        this.assertionConsumerServiceBinding = assertionConsumerServiceBinding;
    }

    public String getSingleLogoutServiceLocation() {
        return singleLogoutServiceLocation;
    }

    public void setSingleLogoutServiceLocation(String singleLogoutServiceLocation) {
        this.singleLogoutServiceLocation = singleLogoutServiceLocation;
    }

    public String getSingleLogoutServiceResponseLocation() {
        return singleLogoutServiceResponseLocation;
    }

    public void setSingleLogoutServiceResponseLocation(String singleLogoutServiceResponseLocation) {
        this.singleLogoutServiceResponseLocation = singleLogoutServiceResponseLocation;
    }

    public Set<String> getSingleLogoutServiceBindings() {
        return singleLogoutServiceBindings;
    }

    public void setSingleLogoutServiceBindings(Set<String> singleLogoutServiceBindings) {
        this.singleLogoutServiceBindings = singleLogoutServiceBindings;
    }

    public String getNameIdFormat() {
        return nameIdFormat;
    }

    public void setNameIdFormat(String nameIdFormat) {
        this.nameIdFormat = nameIdFormat;
    }

    public boolean isAuthnRequestsSigned() {
        return authnRequestsSigned;
    }

    public void setAuthnRequestsSigned(boolean authnRequestsSigned) {
        this.authnRequestsSigned = authnRequestsSigned;
    }

    public Saml2AssertingPartyEntity getAssertingPartyMetadata() {
        return assertingPartyMetadata;
    }

    public void setAssertingPartyMetadata(Saml2AssertingPartyEntity assertingPartyMetadata) {
        this.assertingPartyMetadata = assertingPartyMetadata;
    }

    public Set<SamlCredentialEntity> getDecryptionX509Credentials() {
        return decryptionX509Credentials;
    }

    public void setDecryptionX509Credentials(Set<SamlCredentialEntity> decryptionX509Credentials) {
        this.decryptionX509Credentials = decryptionX509Credentials;
    }

    public Set<SamlCredentialEntity> getSigningX509Credentials() {
        return signingX509Credentials;
    }

    public void setSigningX509Credentials(Set<SamlCredentialEntity> signingX509Credentials) {
        this.signingX509Credentials = signingX509Credentials;
    }
}
