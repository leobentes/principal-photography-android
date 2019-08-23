package com.uarever.principalphotography.util;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by leobentes on 2/19/18.
 */


/*
 * Schedule for updating local DB
 */

public class UpdateLocalDbScheduler extends JobService {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = UpdateLocalDbScheduler.class.getSimpleName();
    public static final String DEBUG_TAG =
            "PPDebug (" + UpdateLocalDbScheduler.class.getSimpleName() + "):";

    @Override
    public boolean onStartJob(JobParameters job) {
        // Rebuild DB
        LocalDb.rebuildLocalDb(this, PPHelper.getMyLocation(this), false);

        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }
}