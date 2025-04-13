package com.hsbc.zmx.transaction.service.supervisor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisionResult {
    private Boolean success = true;
    private String message;
    private String code;

    public static SupervisionResult success() {
        return new SupervisionResult(true, null, null);
    }
}