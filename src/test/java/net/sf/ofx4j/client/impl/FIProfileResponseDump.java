package net.sf.ofx4j.client.impl;

import net.sf.ofx4j.OFXException;
import net.sf.ofx4j.client.FinancialInstitution;
import net.sf.ofx4j.client.FinancialInstitutionData;
import net.sf.ofx4j.client.FinancialInstitutionDataStore;
import net.sf.ofx4j.client.FinancialInstitutionProfile;
import net.sf.ofx4j.domain.data.ResponseEnvelope;
import net.sf.ofx4j.net.OFXConnectionException;
import net.sf.ofx4j.net.OFXV1Connection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * @author Ryan Heaton
 */
public class FIProfileResponseDump {

  public static void main(final String[] args) throws Exception {
    if (args.length < 2) {
      System.out.println("Usage: FIProfileDump <fid> <outFile>");
      System.exit(1);
    }
    FinancialInstitutionDataStore dataStore = new LocalResourceFIDataStore();
    final FinancialInstitutionData fiData = dataStore.getInstitutionData(args[0]);
    OFXV1Connection connection = new OFXV1Connection() {
      @Override
      protected ResponseEnvelope sendBuffer(URL url, ByteArrayOutputStream outBuffer) throws IOException, OFXConnectionException {
        File file = new File(args[1]);
        System.out.println("Writing " + outBuffer.size() + " bytes to " + file.getAbsolutePath() + " for request to " + fiData.getOFXURL() + "...");
        FileOutputStream outFile = new FileOutputStream(file);
        outFile.write(outBuffer.toByteArray());
        return null;
      }
    };

    FinancialInstitution fi = new FinancialInstitutionImpl(fiData, connection) {
      @Override
      protected FinancialInstitutionProfile getProfile(String requestId, ResponseEnvelope response) throws OFXException {
        return null;
      }
    };
    fi.readProfile();
  }
}