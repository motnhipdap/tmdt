package com.dev.dungcony.modules.auth.services.interfaces;

import com.dev.dungcony.modules.auth.dtos.res.AccountRes;

public interface AccountGetService {
    AccountRes getProfileById(int id);
}
