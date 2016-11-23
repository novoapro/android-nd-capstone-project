package com.manpdev.appointment.data.remote;

import com.google.firebase.storage.StorageReference;

/**
 * novoa on 9/24/16.
 */

public interface StorageProvider {
    StorageReference getPublicReference();
}
