package info.weboftrust.ldsignatures.verifier;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

import org.apache.commons.codec.binary.Base64;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.SignatureDecodeException;

import info.weboftrust.ldsignatures.LdSignature;
import info.weboftrust.ldsignatures.suites.EcdsaSecp256k1Signature2019SignatureSuite;
import info.weboftrust.ldsignatures.suites.SignatureSuites;

public class EcdsaSecp256k1Signature2019LdVerifier extends LdVerifier<EcdsaSecp256k1Signature2019SignatureSuite> {

	private Verifier verifier;

	public EcdsaSecp256k1Signature2019LdVerifier(Verifier verifier) {

		super(SignatureSuites.SIGNATURE_SUITE_ECDSASECP256L1SIGNATURE2019);

		this.verifier = verifier;
	}

	public EcdsaSecp256k1Signature2019LdVerifier(ECKey publicKey) {

		this(new PublicKeyVerifier(publicKey));
	}

	public EcdsaSecp256k1Signature2019LdVerifier() {

		this((Verifier) null);
	}

	public static boolean verify(String canonicalizedDocument, LdSignature ldSignature, Verifier verifier) throws GeneralSecurityException {

		// verify

		byte[] canonicalizedDocumentBytes = canonicalizedDocument.getBytes(StandardCharsets.UTF_8);
		byte[] signatureValueBytes = Base64.decodeBase64(ldSignature.getSignatureValue());
		boolean verify = verifier.verify(canonicalizedDocumentBytes, signatureValueBytes);

		// done

		return verify;
	}

	@Override
	public boolean verify(String canonicalizedDocument, LdSignature ldSignature) throws GeneralSecurityException {

		return verify(canonicalizedDocument, ldSignature, this.getVerifier());
	}

	/*
	 * Helper class
	 */

	public interface Verifier {

		public boolean verify(byte[] content, byte[] signature) throws GeneralSecurityException;
	}

	public static class PublicKeyVerifier implements Verifier {

		private ECKey publicKey;

		public PublicKeyVerifier(ECKey publicKey) {

			this.publicKey = publicKey;
		}

		@Override
		public boolean verify(byte[] content, byte[] signature) throws GeneralSecurityException {

			try {

				return this.publicKey.verify(Sha256Hash.hash(content), signature);
			} catch (SignatureDecodeException ex) {
				
				throw new GeneralSecurityException(ex.getMessage(), ex);
			}
		}
	}

	/*
	 * Getters and setters
	 */

	public Verifier getVerifier() {

		return this.verifier;
	}

	public void setVerifier(Verifier verifier) {

		this.verifier = verifier;
	}
}
