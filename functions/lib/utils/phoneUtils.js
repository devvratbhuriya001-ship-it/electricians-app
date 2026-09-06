"use strict";
/**
 * Indian Mobile Phone Number Utilities
 * Strict validation: 10 Indian digits starting with 6, 7, 8, or 9.
 */
Object.defineProperty(exports, "__esModule", { value: true });
exports.normalizeIndianPhoneNumber = normalizeIndianPhoneNumber;
function normalizeIndianPhoneNumber(rawPhone) {
    if (!rawPhone) {
        return { isValid: false, clean10Digits: "", e164: "" };
    }
    // Strip spaces, dashes, brackets, plus
    let digits = rawPhone.replace(/[\s\-\(\)\+]/g, "");
    // Strip international prefix if present
    if (digits.startsWith("91") && digits.length === 12) {
        digits = digits.substring(2);
    }
    else if (digits.startsWith("0") && digits.length === 11) {
        digits = digits.substring(1);
    }
    // Exact 10 digits starting with [6-9]
    const isValid = /^[6-9]\d{9}$/.test(digits);
    return {
        isValid,
        clean10Digits: isValid ? digits : "",
        e164: isValid ? `+91${digits}` : ""
    };
}
//# sourceMappingURL=phoneUtils.js.map