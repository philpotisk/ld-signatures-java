package info.weboftrust.ldsignatures.signer;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

import org.apache.commons.codec.binary.Base64;
import org.bitcoinj.core.ECKey;

import info.weboftrust.ldsignatures.crypto.ByteSigner;
import info.weboftrust.ldsignatures.crypto.impl.P256K_ES256K_PrivateKeySigner;
import info.weboftrust.ldsignatures.suites.EcdsaKoblitzSignature2016SignatureSuite;
import info.weboftrust.ldsignatures.suites.SignatureSuites;

public class EcdsaKoblitzSignature2016LdSigner extends LdSigner<EcdsaKoblitzSignature2016SignatureSuite> {

	public EcdsaKoblitzSignature2016LdSigner(ByteSigner signer) {

		super(SignatureSuites.SIGNATURE_SUITE_ECDSAKOBLITZSIGNATURE2016, signer);
	}

	public EcdsaKoblitzSignature2016LdSigner(ECKey privateKey) {

		this(new P256K_ES256K_PrivateKeySigner(privateKey));
	}

	public EcdsaKoblitzSignature2016LdSigner() {

		this((ByteSigner) null);
	}

	public static String sign(String canonicalizedDocument, ByteSigner signer) throws GeneralSecurityException {

		// sign

		byte[] canonicalizedDocumentBytes = canonicalizedDocument.getBytes(StandardCharsets.UTF_8);
		byte[] signatureBytes = signer.sign(canonicalizedDocumentBytes, "ES256K");
		String signatureString = Base64.encodeBase64String(signatureBytes);

		// done

		return signatureString;
	}

	@Override
	public String sign(String canonicalizedDocument) throws GeneralSecurityException {

		return sign(canonicalizedDocument, this.getSigner());
	}
}
