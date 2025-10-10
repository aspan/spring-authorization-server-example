package com.example.security.persistence.service;


import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ssl.pem.PemContent;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.Saml2MessageBinding;
import org.springframework.stereotype.Service;

import com.example.security.persistence.entity.Saml2AssertingPartyEntity;
import com.example.security.persistence.entity.SamlCredentialEntity;
import com.example.security.persistence.entity.SamlRelyingPartyEntity;
import com.example.security.persistence.repository.SamlRelyingPartyEntityRepository;

@Service
public class SamlRelyingPartyRegistrationRepository implements RelyingPartyRegistrationRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(SamlRelyingPartyRegistrationRepository.class);
    private final SamlRelyingPartyEntityRepository samlRelyingPartyEntityRepository;

    public SamlRelyingPartyRegistrationRepository(SamlRelyingPartyEntityRepository samlRelyingPartyEntityRepository) {
        this.samlRelyingPartyEntityRepository = samlRelyingPartyEntityRepository;
    }

    @Override
    public @Nullable RelyingPartyRegistration findByRegistrationId(@NonNull String registrationId) {
        return samlRelyingPartyEntityRepository.findById(registrationId).map(SamlRelyingPartyRegistrationRepository::fromEntity).orElse(null);
    }


    @Override
    public @Nullable RelyingPartyRegistration findUniqueByAssertingPartyEntityId(@NonNull String entityId) {
        return samlRelyingPartyEntityRepository.findByAssertingPartyMetadata_entityId(entityId).map(SamlRelyingPartyRegistrationRepository::fromEntity).orElse(null);
    }

    private static @NonNull RelyingPartyRegistration fromEntity(@NonNull SamlRelyingPartyEntity samlRelyingPartyEntity) {
        var assertingPartyMetadata = samlRelyingPartyEntity.getAssertingPartyMetadata() != null ? samlRelyingPartyEntity.getAssertingPartyMetadata() : new Saml2AssertingPartyEntity();
        return RelyingPartyRegistration.withRegistrationId(samlRelyingPartyEntity.getRegistrationId())
                                       .entityId(samlRelyingPartyEntity.getEntityId())
                                       .assertionConsumerServiceLocation(samlRelyingPartyEntity.getAssertionConsumerServiceLocation())
                                       .assertionConsumerServiceBinding(Optional.ofNullable(samlRelyingPartyEntity.getAssertionConsumerServiceBinding()).map(Saml2MessageBinding::valueOf).orElse(null))
                                       .singleLogoutServiceLocation(samlRelyingPartyEntity.getSingleLogoutServiceLocation())
                                       .singleLogoutServiceResponseLocation(samlRelyingPartyEntity.getSingleLogoutServiceResponseLocation())
                                       .singleLogoutServiceBindings(b -> b.addAll(samlRelyingPartyEntity.getSingleLogoutServiceBindings().stream().map(Saml2MessageBinding::valueOf).toList()))
                                       .nameIdFormat(samlRelyingPartyEntity.getNameIdFormat())
                                       .authnRequestsSigned(samlRelyingPartyEntity.isAuthnRequestsSigned())
                                       .assertingPartyMetadata((assertingPartyBuilder) ->
                                                                       assertingPartyBuilder.entityId(assertingPartyMetadata.getEntityId())
                                                                                            .wantAuthnRequestsSigned(assertingPartyMetadata.isWantAuthnRequestsSigned())
                                                                                            .signingAlgorithms(l -> l.addAll(assertingPartyMetadata.getSigningAlgorithms()))
                                                                                            .singleLogoutServiceLocation(assertingPartyMetadata.getSingleLogoutServiceLocation())
                                                                                            .singleLogoutServiceResponseLocation(assertingPartyMetadata.getSingleLogoutServiceResponseLocation())
                                                                                            .singleLogoutServiceBinding(Optional.ofNullable(assertingPartyMetadata.getSingleLogoutServiceBinding()).map(Saml2MessageBinding::valueOf).orElse(null))
                                                                                            .singleSignOnServiceBinding(Optional.ofNullable(assertingPartyMetadata.getSingleSignOnServiceBinding()).map(Saml2MessageBinding::valueOf).orElse(null))
                                                                                            .singleSignOnServiceLocation(assertingPartyMetadata.getSingleSignOnServiceLocation())
                                                                                            .encryptionX509Credentials(d -> d.addAll(assertingPartyMetadata.getEncryptionX509Credentials().stream().map(SamlRelyingPartyRegistrationRepository::encryption).toList()))
                                                                                            .verificationX509Credentials(d -> d.addAll(assertingPartyMetadata.getVerificationX509Credentials().stream().map(SamlRelyingPartyRegistrationRepository::verification).toList()))
                                       )
                                       .decryptionX509Credentials(d -> d.addAll(samlRelyingPartyEntity.getDecryptionX509Credentials().stream().map(SamlRelyingPartyRegistrationRepository::decryption).toList()))
                                       .signingX509Credentials(d -> d.addAll(samlRelyingPartyEntity.getSigningX509Credentials().stream().map(SamlRelyingPartyRegistrationRepository::signing).toList()))
                                       .build();
    }

    private static @NonNull SamlRelyingPartyEntity toEntity(@NonNull RelyingPartyRegistration relyingPartyRegistration) {
        var samlRelyingParty = new SamlRelyingPartyEntity();
        samlRelyingParty.setRegistrationId(relyingPartyRegistration.getRegistrationId());
        samlRelyingParty.setEntityId(relyingPartyRegistration.getEntityId());
        samlRelyingParty.setAssertionConsumerServiceLocation(relyingPartyRegistration.getAssertionConsumerServiceLocation());
        samlRelyingParty.setAssertionConsumerServiceBinding(Optional.ofNullable(relyingPartyRegistration.getAssertionConsumerServiceBinding()).map(Enum::name).orElse(null));
        samlRelyingParty.setSingleLogoutServiceLocation(relyingPartyRegistration.getSingleLogoutServiceLocation());
        samlRelyingParty.setSingleLogoutServiceResponseLocation(relyingPartyRegistration.getSingleLogoutServiceResponseLocation());
        samlRelyingParty.setSingleLogoutServiceBindings(samlRelyingParty.getSingleLogoutServiceBindings());
        samlRelyingParty.setNameIdFormat(relyingPartyRegistration.getNameIdFormat());
        samlRelyingParty.setAuthnRequestsSigned(relyingPartyRegistration.isAuthnRequestsSigned());
        var samlAssertingParty = new Saml2AssertingPartyEntity();
        samlAssertingParty.setEntityId(relyingPartyRegistration.getAssertingPartyMetadata().getEntityId());
        samlAssertingParty.setWantAuthnRequestsSigned(relyingPartyRegistration.getAssertingPartyMetadata().getWantAuthnRequestsSigned());
        samlAssertingParty.setSigningAlgorithms(samlAssertingParty.getSigningAlgorithms());
        samlAssertingParty.setSingleLogoutServiceLocation(relyingPartyRegistration.getAssertingPartyMetadata().getSingleLogoutServiceLocation());
        samlAssertingParty.setSingleLogoutServiceResponseLocation(relyingPartyRegistration.getAssertingPartyMetadata().getSingleLogoutServiceResponseLocation());
        samlAssertingParty.setSingleLogoutServiceBinding(Optional.ofNullable(relyingPartyRegistration.getAssertingPartyMetadata().getSingleLogoutServiceBinding()).map(Enum::name).orElse(null));
        samlAssertingParty.setSingleSignOnServiceBinding(Optional.ofNullable(relyingPartyRegistration.getAssertingPartyMetadata().getSingleSignOnServiceBinding()).map(Enum::name).orElse(null));
        samlAssertingParty.setSingleSignOnServiceLocation(relyingPartyRegistration.getAssertingPartyMetadata().getSingleSignOnServiceLocation());
        samlAssertingParty.setVerificationX509Credentials(relyingPartyRegistration.getAssertingPartyMetadata().getVerificationX509Credentials().stream().map(SamlRelyingPartyRegistrationRepository::getSamlCredential).collect(Collectors.toSet()));
        samlAssertingParty.setEncryptionX509Credentials(relyingPartyRegistration.getAssertingPartyMetadata().getEncryptionX509Credentials().stream().map(SamlRelyingPartyRegistrationRepository::getSamlCredential).collect(Collectors.toSet()));
        samlRelyingParty.setAssertingPartyMetadata(samlAssertingParty);
        samlRelyingParty.setDecryptionX509Credentials(relyingPartyRegistration.getDecryptionX509Credentials().stream().map(SamlRelyingPartyRegistrationRepository::getSamlCredential).collect(Collectors.toSet()));
        samlRelyingParty.setSigningX509Credentials(relyingPartyRegistration.getSigningX509Credentials().stream().map(SamlRelyingPartyRegistrationRepository::getSamlCredential).collect(Collectors.toSet()));
        return samlRelyingParty;
    }

    private static @NonNull SamlCredentialEntity getSamlCredential(@NonNull Saml2X509Credential c) {
        var samlCredentials = new SamlCredentialEntity();
        samlCredentials.setCertificate(toPemContent(c.getCertificate()));
        samlCredentials.setPrivateKey(toPemContent(c.getPrivateKey()));
        return samlCredentials;
    }

    private static @Nullable String toPemContent(@Nullable X509Certificate certificate) {
        if (certificate == null) {
            return null;
        }
        var encoder = Base64.getMimeEncoder(64, "\n".getBytes(StandardCharsets.UTF_8));
        var sb = new StringBuilder("-----BEGIN CERTIFICATE-----\n");
        try {
            sb.append(encoder.encodeToString(certificate.getEncoded()));
        } catch (CertificateEncodingException e) {
            throw new RuntimeException(e);
        }
        sb.append("\n-----END CERTIFICATE-----\n");
        return sb.toString();
    }

    private static @Nullable String toPemContent(@Nullable PrivateKey privateKey) {
        if (privateKey == null) {
            return null;
        }
        var encoder = Base64.getMimeEncoder(64, "\n".getBytes(StandardCharsets.UTF_8));
        var sb = new StringBuilder("-----BEGIN PRIVATE KEY-----\n");
        sb.append(encoder.encodeToString(privateKey.getEncoded()));
        sb.append("\n-----END PRIVATE KEY-----\n");
        return sb.toString();
    }

    private static @NonNull Saml2X509Credential decryption(@NonNull SamlCredentialEntity samlCredentialEntity) {
        return Saml2X509Credential.decryption(readPrivateKey(samlCredentialEntity.getPrivateKey()), readCertificate(samlCredentialEntity.getCertificate()));
    }

    private static @NonNull Saml2X509Credential encryption(@NonNull SamlCredentialEntity samlCredentialEntity) {
        return Saml2X509Credential.encryption(readCertificate(samlCredentialEntity.getCertificate()));
    }

    private static @NonNull Saml2X509Credential signing(@NonNull SamlCredentialEntity samlCredentialEntity) {
        return Saml2X509Credential.signing(readPrivateKey(samlCredentialEntity.getPrivateKey()), readCertificate(samlCredentialEntity.getCertificate()));
    }

    private static @NonNull Saml2X509Credential verification(@NonNull SamlCredentialEntity samlCredentialEntity) {
        return Saml2X509Credential.verification(readCertificate(samlCredentialEntity.getCertificate()));
    }

    private static @NonNull RSAPrivateKey readPrivateKey(@NonNull String pk) {
        try {
            return (RSAPrivateKey) PemContent.of(pk).getPrivateKey();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static @NonNull X509Certificate readCertificate(@NonNull String certificate) {
        try {
            return PemContent.of(certificate).getCertificates().getFirst();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public void save(@NonNull RelyingPartyRegistration relyingPartyRegistration) {
        LOGGER.debug("Saving registration {}", relyingPartyRegistration.getRegistrationId());
        samlRelyingPartyEntityRepository.save(toEntity(relyingPartyRegistration));
    }
}
