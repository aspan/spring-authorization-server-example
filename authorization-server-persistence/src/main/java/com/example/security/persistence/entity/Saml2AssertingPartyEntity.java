package com.example.security.persistence.entity;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "security_saml2_asserting_party")
public class Saml2AssertingPartyEntity {
    @Id
    @Column(name = "entity_id")
    private String entityId;
    @Column(name = "want_authn_requests_signed")
    private boolean wantAuthnRequestsSigned;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "security_saml2_asserting_party_signing_algorithms",
            catalog = "authentication_server",
            joinColumns = @JoinColumn(name = "entity_id", foreignKey = @ForeignKey(name = "fk_ap_sa_entity_id"), referencedColumnName = "entity_id")
    )
    @Column(name = "signing_algorithm")
    private Set<String> signingAlgorithms;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable
            (
                    name = "security_saml2_asserting_party_verification_credentials",
                    catalog = "authentication_server",
                    joinColumns = {@JoinColumn(name = "entity_id", foreignKey = @ForeignKey(name = "fk_ap_vc_reg_id"), referencedColumnName = "entity_id")},
                    inverseJoinColumns = {@JoinColumn(name = "credentials_id", foreignKey = @ForeignKey(name = "fk_ap_vc_cred_id"), referencedColumnName = "id", unique = true)},
                    uniqueConstraints = {@UniqueConstraint(name = "uk_ap_vc_cred_id", columnNames = {"credentials_id"})}
            )
    private Set<SamlCredentialEntity> verificationX509Credentials;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable
            (
                    name = "security_saml2_asserting_party_encryption_credentials",
                    catalog = "authentication_server",
                    joinColumns = {@JoinColumn(name = "entity_id", foreignKey = @ForeignKey(name = "fk_ap_ec_reg_id"), referencedColumnName = "entity_id")},
                    inverseJoinColumns = {@JoinColumn(name = "credentials_id", foreignKey = @ForeignKey(name = "fk_ap_ec_cred_id"), referencedColumnName = "id", unique = true)},
                    uniqueConstraints = {@UniqueConstraint(name = "uk_ap_ec_cred_id", columnNames = {"credentials_id"})}
            )
    private Set<SamlCredentialEntity> encryptionX509Credentials;
    @Column(name = "single_sign_on_service_location")
    private String singleSignOnServiceLocation;
    @Column(name = "single_sign_on_service_binding", length = 10)
    private String singleSignOnServiceBinding;
    @Column(name = "single_logout_service_location")
    private String singleLogoutServiceLocation;
    @Column(name = "single_logout_service_response_location")
    private String singleLogoutServiceResponseLocation;
    @Column(name = "single_logout_service_binding", length = 10)
    private String singleLogoutServiceBinding;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public boolean isWantAuthnRequestsSigned() {
        return wantAuthnRequestsSigned;
    }

    public void setWantAuthnRequestsSigned(boolean wantAuthnRequestsSigned) {
        this.wantAuthnRequestsSigned = wantAuthnRequestsSigned;
    }

    public Set<String> getSigningAlgorithms() {
        return signingAlgorithms;
    }

    public void setSigningAlgorithms(Set<String> signingAlgorithms) {
        this.signingAlgorithms = signingAlgorithms;
    }

    public Set<SamlCredentialEntity> getVerificationX509Credentials() {
        return verificationX509Credentials;
    }

    public void setVerificationX509Credentials(Set<SamlCredentialEntity> verificationX509Credentials) {
        this.verificationX509Credentials = verificationX509Credentials;
    }

    public Set<SamlCredentialEntity> getEncryptionX509Credentials() {
        return encryptionX509Credentials;
    }

    public void setEncryptionX509Credentials(Set<SamlCredentialEntity> encryptionX509Credentials) {
        this.encryptionX509Credentials = encryptionX509Credentials;
    }

    public String getSingleSignOnServiceLocation() {
        return singleSignOnServiceLocation;
    }

    public void setSingleSignOnServiceLocation(String singleSignOnServiceLocation) {
        this.singleSignOnServiceLocation = singleSignOnServiceLocation;
    }

    public String getSingleSignOnServiceBinding() {
        return singleSignOnServiceBinding;
    }

    public void setSingleSignOnServiceBinding(String singleSignOnServiceBinding) {
        this.singleSignOnServiceBinding = singleSignOnServiceBinding;
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

    public String getSingleLogoutServiceBinding() {
        return singleLogoutServiceBinding;
    }

    public void setSingleLogoutServiceBinding(String singleLogoutServiceBinding) {
        this.singleLogoutServiceBinding = singleLogoutServiceBinding;
    }
}
