package fr.personnel.southsayerbackend.utils.sftp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author Farouk KABOUCHE
 * SFTP File Utils
 * @version 1.0
 */
@Slf4j
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SFTPFileUtils {

    @Value("${sftp.username}")
    private String username;
    @Value("${sftp.password}")
    private String password;
    @Value("${sftp.host}")
    private String host;
    @Value("${sftp.port}")
    private int port;

    public boolean uploadSFTP(String localPath, String fileName, String targetPath) throws IOException {

        boolean result = false;
        SSHClient sshClient = setupSshj();
        SFTPClient sftpClient = sshClient.newSFTPClient();

        sftpClient.put(localPath + "/" + fileName, targetPath + "/" + fileName);

        if(!sftpClient.ls(targetPath).isEmpty()) result = true;
        sftpClient.close();
        return result;
    }
    public boolean downloadSFTP(String localPath, String fileName, String targetPath) throws IOException {

        boolean result = false;
        SSHClient sshClient = setupSshj();
        SFTPClient sftpClient = sshClient.newSFTPClient();

        sftpClient.get(targetPath + "/" + fileName, localPath + "/" + fileName);

        File file = new File(localPath + "/" + fileName);
        if(file.exists()) result = true;

        sftpClient.close();
        return result;
    }

    private SSHClient setupSshj() throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new PromiscuousVerifier());
        client.connect(host);
        client.authPassword(username, password);
        return client;
    }
}
