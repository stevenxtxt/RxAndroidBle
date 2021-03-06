package com.polidea.rxandroidble2.helpers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.polidea.rxandroidble2.ClientComponent;
import com.polidea.rxandroidble2.DaggerClientComponent;
import com.polidea.rxandroidble2.internal.util.DisposableUtil;

import java.util.UUID;

import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;

/**
 * An Observable that emits false if an attempt to scan with {@link com.polidea.rxandroidble2.RxBleClient#scanBleDevices(UUID...)}
 * would cause the exception {@link com.polidea.rxandroidble2.exceptions.BleScanException#LOCATION_SERVICES_DISABLED}; otherwise emits true.
 * Always emits true in Android versions prior to 6.0.
 * Typically, receiving false should cause the user to be prompted to enable Location Services.
 */
public class LocationServicesOkObservable extends Observable<Boolean> {

    @NonNull
    private final Observable<Boolean> locationServicesOkObsImpl;

    public static LocationServicesOkObservable createInstance(@NonNull final Context context) {
        return DaggerClientComponent
                .builder()
                .clientModule(new ClientComponent.ClientModule(context))
                .build()
                .locationServicesOkObservable();
    }

    @Inject
    LocationServicesOkObservable(
            @NonNull
            @Named(ClientComponent.NamedBooleanObservables.LOCATION_SERVICES_OK) final Observable<Boolean> locationServicesOkObsImpl) {
        this.locationServicesOkObsImpl = locationServicesOkObsImpl;
    }

    @Override
    protected void subscribeActual(final Observer<? super Boolean> observer) {
        final DisposableObserver<? super Boolean> disposableObserver = DisposableUtil.disposableObserver(observer);
        locationServicesOkObsImpl.subscribeWith(disposableObserver);
        observer.onSubscribe(disposableObserver);
    }
}
