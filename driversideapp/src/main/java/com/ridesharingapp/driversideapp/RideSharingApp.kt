package com.ridesharingapp.driversideapp

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.maps.GeoApiContext
import com.ridesharingapp.common.google.GoogleService
import com.ridesharingapp.common.services.AuthenticationService
import com.ridesharingapp.common.services.FirebaseAuthService
import com.ridesharingapp.common.services.FirebaseHistoryService
import com.ridesharingapp.common.services.FirebasePhotoService
import com.ridesharingapp.common.services.HistoryService
import com.ridesharingapp.common.services.RideService
import com.ridesharingapp.common.services.StreamRideService
import com.ridesharingapp.common.services.StreamUserService
import com.ridesharingapp.common.services.UserService
import com.ridesharingapp.common.usecases.GetUser
import com.ridesharingapp.common.usecases.LogInUser
import com.ridesharingapp.common.usecases.LogOutUser
import com.ridesharingapp.common.usecases.SignUpUser
import com.ridesharingapp.common.usecases.UpdateUserAvatar
import com.zhuinden.simplestack.GlobalServices
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.rebind
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory

class RideSharingApp : Application() {
    lateinit var globalServices: GlobalServices
    lateinit var geoContext: GeoApiContext

    override fun onCreate() {
        super.onCreate()


//        // dev services
//        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
//        // the host computer from an Android emulator.
//        val firestore = Firebase.firestore
//        firestore.useEmulator("10.0.2.2", 8080)
//        firestore.firestoreSettings = firestoreSettings {
//            isPersistenceEnabled = false
//        }
//        Firebase.auth.useEmulator("10.0.2.2", 9099)


        MapsInitializer.initialize(this)
        geoContext = GeoApiContext.Builder()
            .apiKey(BuildConfig.MAPS_API_KEY)
            .build()
        val streamClient = configureStream()

        val firebaseAuthService = FirebaseAuthService(FirebaseAuth.getInstance())
        val firebaseStorageService = FirebasePhotoService(FirebaseStorage.getInstance(), this)

        val streamUserService = StreamUserService(streamClient)
        val streamRideService = StreamRideService(streamClient)

        val googleService = GoogleService(this, geoContext)

        /*
        Usecases:
        - In situations where multiple BE services must be coordinated in order to carry out a
        single function, a usecase is employed. However, In situations where a single call to a
        single BE service is required, there does not tend to be any benefit to adding usecases.
         */
        val getUser = GetUser(firebaseAuthService, streamUserService)
        val signUpUser = SignUpUser(firebaseAuthService, streamUserService)
        val logInUser = LogInUser(firebaseAuthService, streamUserService)
        val logOutUser = LogOutUser(firebaseAuthService, streamUserService)
        val updateUserAvatar = UpdateUserAvatar(firebaseStorageService, streamUserService)

        val historyService = FirebaseHistoryService()

        globalServices = GlobalServices.builder()
            .add(streamRideService)
            .rebind<RideService>(streamRideService)
            .add(streamUserService)
            .rebind<UserService>(streamUserService)
            .add(firebaseAuthService)
            .rebind<AuthenticationService>(firebaseAuthService)
            .add(googleService)
            .add(getUser)
            .add(signUpUser)
            .add(logInUser)
            .add(logOutUser)
            .add(updateUserAvatar)
            .add(streamClient)
            .add(historyService)
            .rebind<HistoryService>(historyService)
            .build()
    }

    private fun configureStream(): ChatClient {
        val logLevel = if (BuildConfig.DEBUG) ChatLogLevel.ALL else ChatLogLevel.NOTHING
        val pluginFactory = StreamOfflinePluginFactory(
            config = Config(
                backgroundSyncEnabled = true,
                userPresence = true,
                persistenceEnabled = true,
                uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING,
            ),
            appContext = this,
        )

        return ChatClient.Builder(BuildConfig.STREAM_API_KEY, this)
            .withPlugin(pluginFactory)
            .logLevel(logLevel)
            .build()
    }
}