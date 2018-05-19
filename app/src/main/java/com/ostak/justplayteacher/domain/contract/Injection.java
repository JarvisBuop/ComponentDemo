package com.ostak.justplayteacher.domain.contract;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jarvisdong.uikit.clear.UseCaseHandler;
import com.jarvisdong.uikit.clear.concreate.CommonUseCase;
import com.jarvisdong.uikit.clear.concreate.DatasRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by JarvisDong on 2018/2/6.
 */

public class Injection {
    public static DatasRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        return DatasRepository.getInstance(CommonRemoteRepository.getInstance(),
                CommonLocalRepository.getInstance());
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }

    /**
     * usecase
     */
    public static CommonUseCase provideCommonUseCase(@NonNull Context context) {
        return new CommonUseCase(Injection.provideTasksRepository(context));
    }

    public static void cancelUseCase(CommonUseCase mCommonUseCase) {
        if (mCommonUseCase != null)
            mCommonUseCase.cancel();
    }
}
