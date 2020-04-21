package com.varrock.util;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jonny on 9/4/2019
 **/
public class GitDeploy {

    public static PullResult deploy() {

        try {

            Repository existingRepo = new FileRepositoryBuilder()
                    .setGitDir(new File("./.git"))
                    .build();

            Git git = new Git(existingRepo);

            PullCommand pc = git.pull().setCredentialsProvider( new UsernamePasswordCredentialsProvider( "jonathansirens", "gitpasswordgoeshere" ));
            return pc.call();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (RefNotAdvertisedException e) {
            e.printStackTrace();
        } catch (NoHeadException e) {
            e.printStackTrace();
        } catch (TransportException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (InvalidRemoteException e) {
            e.printStackTrace();
        } catch (CanceledException e) {
            e.printStackTrace();
        } catch (WrongRepositoryStateException e) {
            e.printStackTrace();
        } catch (RefNotFoundException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return null;
    }

}
