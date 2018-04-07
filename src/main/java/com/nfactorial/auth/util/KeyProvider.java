package com.nfactorial.auth.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.nfactorial.auth.constant.AppConstants;

import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

@Component
public class KeyProvider {
	
	
	private X509Certificate generateCertificate(String dn, KeyPair keyPair, int validity, String sigAlgName) throws GeneralSecurityException, IOException {
	    PrivateKey privateKey = keyPair.getPrivate();

	    X509CertInfo info = new X509CertInfo();

	    Date from = new Date();
	    Date to = new Date(from.getTime() + validity * 1000L * 24L * 60L * 60L);

	    CertificateValidity interval = new CertificateValidity(from, to);
	    BigInteger serialNumber = new BigInteger(64, new SecureRandom());
	    X500Name owner = new X500Name(dn);
	    AlgorithmId sigAlgId = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);

	    info.set(X509CertInfo.VALIDITY, interval);
	    info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(serialNumber));
	    info.set(X509CertInfo.SUBJECT, owner);
	    info.set(X509CertInfo.ISSUER, owner);
	    info.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic()));
	    info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
	    info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(sigAlgId));

	    // Sign the cert to identify the algorithm that's used.
	    X509CertImpl certificate = new X509CertImpl(info);
	    //certificate.sign(privateKey, sigAlgName);
	    sigAlgId = (AlgorithmId) certificate.get(X509CertImpl.SIG_ALG);
	    info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, sigAlgId);
	    certificate = new X509CertImpl(info);
	    certificate.sign(privateKey, sigAlgName);

	    return certificate;
	}

	
	
	//  @Scheduled(fixedRate = 5000)    // Todo create new keypair every 3 hrs and discard the old one 
	public void createAndStoreCert(String dn,String alias) {
		KeyPairGenerator keyPairGenerator = null;
		
	       try {
	    	    keyPairGenerator = KeyPairGenerator.getInstance("RSA");  // get algorithm from configuration server
		        keyPairGenerator.initialize(4096);							// get the size from configuaration server		
		        KeyPair keyPair = keyPairGenerator.generateKeyPair();
		        KeyStore keyStore = getKeyStore();
	        	Certificate[] chain = {generateCertificate(dn, keyPair, 365, AppConstants.SIGNINGALGO)};  // get Signing algorithm from configuration server
	        	keyStore.setKeyEntry(alias, keyPair.getPrivate(), AppConstants.keyStorePassword.toCharArray(), chain); // Get the password from configuration server
	        	keyStore.store(new FileOutputStream(getKeyStoreFile()), AppConstants.keyStorePassword.toCharArray()); // Get the password from configuration server
			} catch (KeyStoreException e) {
				// Send this to kafka topic
				e.printStackTrace();
			} catch (CertificateException e) {
				// Send this to kafka topic
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// Send this to kafka topic
				e.printStackTrace();
			} catch (IOException e) {
				// Send this to kafka topic
				e.printStackTrace();
			} catch (GeneralSecurityException e) {
				// Send this to kafka topic
				e.printStackTrace();
			} catch (Exception e) {
				// Send this to kafka topic
				e.printStackTrace();
			
			}

	}
	private File getKeyStoreFile() {
		File file = new File(AppConstants.keyStoreName); // Get the password from configuration server
		return file;
	}
	
	public Key getPublicKey(String alias) {
		
		try {
			KeyStore keyStore= getKeyStore();
			Certificate certificate=keyStore.getCertificate(alias);
			return  certificate.getPublicKey(); //keyStore.getKey(alias,"wintermore".toCharArray()); // Get the password from configuration server
		} catch (UnrecoverableKeyException e) {
			// Send this to kafka topic
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// Send this to kafka topic
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// send this to kafka topic
			e.printStackTrace();
		} catch (Exception e) {
			// send this to kafka topic
			e.printStackTrace();
		}
		return null;
	}
	
public Key getPrivateKey(String alias) {
		
		try {
			KeyStore keyStore= getKeyStore();
			return  keyStore.getKey(alias,AppConstants.keyStorePassword.toCharArray()); // Get the password from configuration server
		} catch (UnrecoverableKeyException e) {
			// Send this to kafka topic
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// Send this to kafka topic
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// send this to kafka topic
			e.printStackTrace();
		} catch (Exception e) {
			// send this to kafka topic
			e.printStackTrace();
		}
		return null;
	}
	
	private  KeyStore getKeyStore() throws Exception {
	    	File file=getKeyStoreFile();
	    	KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
	    if (file.exists()) {
	        keyStore.load(new FileInputStream(file), AppConstants.keyStorePassword.toCharArray()); // Get the password from configuration server
	    } else {
	        
	        keyStore.load(null, null);
	        keyStore.store(new FileOutputStream(file), AppConstants.keyStorePassword.toCharArray()); // Get the password from configuration server
	    }
	    return keyStore;
	}
}
