package com.lzh.po;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.identity.X509Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoPrimitives;

public class LocalUser implements User {
	
	private String name;
	private String mspId;
	private Enrollment enrollment;
	
	public LocalUser(String name, String mspId) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.mspId = mspId;
	}
	
	public LocalUser(String name, String mspId, String keyFile, String certFile) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.mspId = mspId;
		try {
			this.enrollment = loadFromPemFile(keyFile, certFile);
		} catch (CryptoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Enrollment loadFromPemFile(String keyFile,String certFile) throws CryptoException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
		byte[] keyPem = Files.readAllBytes(Paths.get(keyFile));
		byte[] certPem = Files.readAllBytes(Paths.get(certFile));
		CryptoPrimitives suite = new CryptoPrimitives();
		PrivateKey privateKey = suite.bytesToPrivateKey(keyPem);
		return new X509Enrollment(privateKey,new String(certPem));
	}
	
	
	
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	public Set<String> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAffiliation() {
		// TODO Auto-generated method stub
		return null;
	}

	public Enrollment getEnrollment() {
		// TODO Auto-generated method stub
		return this.enrollment;
	}

	public String getMspId() {
		// TODO Auto-generated method stub
		return this.mspId;
	}

}
