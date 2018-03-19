package com.flatlaylib.service.res;

import com.flatlaylib.bean.BrandResult;
import com.flatlaylib.bean.Flatlays;
import com.flatlaylib.bean.ProductResult;
import com.flatlaylib.bean.StroeResult;
import com.flatlaylib.bean.UserResult;

import java.util.List;

/**
 * Created by RachelDi on 3/15/18.
 */

public class GeneralSearchRes {
    String code;
    List<UserResult> users;
    Flatlays flatlays;
    List<ProductResult> products;
    List<StroeResult> stores;
    List<BrandResult> brands;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<UserResult> getUsers() {
        return users;
    }

    public void setUsers(List<UserResult> users) {
        this.users = users;
    }

    public Flatlays getFlatlays() {
        return flatlays;
    }

    public void setFlatlays(Flatlays flatlays) {
        this.flatlays = flatlays;
    }

    public List<ProductResult> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResult> products) {
        this.products = products;
    }

    public List<StroeResult> getStores() {
        return stores;
    }

    public void setStores(List<StroeResult> stores) {
        this.stores = stores;
    }

    public List<BrandResult> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandResult> brands) {
        this.brands = brands;
    }
}

