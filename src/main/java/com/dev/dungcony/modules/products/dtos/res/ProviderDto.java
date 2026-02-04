package com.dev.dungcony.modules.products.dtos.res;

import com.dev.dungcony.modules.products.entities.Provider;

public record ProviderDto(
        Integer id,
        String name,
        String description,
        String logo,
        String email,
        String phone
) {
    public Provider getEntity(ProviderDto p) {
        Provider ans = new Provider();
        ans.setId(p.id());
        ans.setName(p.name());
        ans.setDescription(p.description());
        ans.setEmail(p.email());
        ans.setPhone(p.phone());
        ans.setLogo(logo);

        return ans;
    }

    public ProviderDto(Provider p) {
        this(p.getId(), p.getName(), p.getDescription(), p.getLogo(), p.getEmail(), p.getPhone());
    }
}
