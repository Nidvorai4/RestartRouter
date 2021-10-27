// Copyright 2020 Igor Tolmachev, ITSamples.com. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License"); /
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ==============================================================================

package twelvecabsoft.education.restartrouter;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;


class TelnetConnection
{
    private TelnetClient mClient;

    TelnetConnection() {
        mClient = new TelnetClient();
        mClient.setConnectTimeout(5000);
    }

    void connect(String serverAddress, int serverPort) throws IOException {
        try {
            mClient.connect(serverAddress, serverPort);
        }
        catch (SocketException ex) {
            throw new SocketException("Connection error...");
        }
    }

    BufferedInputStream getInput(){
        return new BufferedInputStream(mClient.getInputStream());
    }

    boolean isConnected() {
        return mClient.isConnected();
    }

    boolean sendCommand(String command){
        if(mClient == null || !mClient.isConnected()) {
            return false;
        }

        byte[] bytes = command.getBytes();

        boolean binary = command.toLowerCase().contains("0x");
        if(binary) {
            int count = 0;
            byte[] tempArray = new byte[command.length() / 2];
            String[] values = command.split("\\s+");
            for (String s: values) {
                if(s.toLowerCase().contains("0x") && s.length() == 4) {
                    try {
                        tempArray[count] = (byte) Integer.parseInt(s.substring(2), 16);
                        count++;
                    }
                    catch (NumberFormatException x) {
                        x.printStackTrace();
                    }
                }
            }

            if(count > 0) {
                bytes = new byte[count];
                System.arraycopy(tempArray, 0, bytes, 0, count);
            }
        }

        OutputStream outputStream = mClient.getOutputStream();

        try {
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
        }
        catch (Exception e) {
            return false;
        }

        return true;
    }

    boolean disconnect() {
        try {
            mClient.disconnect();
        }
        catch (Exception e) {
            return false;
        }

        return true;
    }

    TelnetClient getConnection(){
        return mClient;
    }
}

