package com.donchacko.ieee754calc;

public class NumberConverter {

    private static final long MAX_INT_32 = Integer.MAX_VALUE;
    private static final long MIN_INT_32 = Integer.MIN_VALUE;
    
    private static boolean isWithinInt32Range(long value) {
        return value >= MIN_INT_32 && value <= MAX_INT_32;
    }
    
    private static boolean isWithinInt32Range(double value) {
        return value >= MIN_INT_32 && value <= MAX_INT_32 && 
               value == (long) value; 
    }
    public static boolean isValidDecimal(String decimal){
        if (decimal == null || decimal.trim().isEmpty()) return false;
        try{
            Double.parseDouble(decimal.trim());
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isValidBinary(String binary){
        if(binary == null || binary.trim().isEmpty()) return false;
        binary = binary.trim();
        boolean isNegative = binary.startsWith("-");
        if (isNegative) binary = binary.substring(1);
        if (binary.isEmpty()) return false;
        
        int dotIndex = binary.indexOf(".");
        String intPart = dotIndex >= 0 ? binary.substring(0, dotIndex) : binary;
        String fracPart = dotIndex >= 0 ? binary.substring(dotIndex + 1) : "";
        
        if (intPart.isEmpty() && fracPart.isEmpty()) return false;
        
        for (int i = 0; i < intPart.length(); i++) {
            char c = intPart.charAt(i);
            if (c != '0' && c != '1') return false;
        }
        
        for (int i = 0; i < fracPart.length(); i++) {
            char c = fracPart.charAt(i);
            if (c != '0' && c != '1') return false;
        }
        
        return true;
    }

    public static boolean isValidTwosComplement(String twosComp){
        if (twosComp == null || twosComp.trim().isEmpty()) return false;
        twosComp = twosComp.trim().replaceAll("\\s", "");
        if (twosComp.length() == 0 || twosComp.length() > 32) return false;
        for(int i = 0; i < twosComp.length(); i++){
            char c = twosComp.charAt(i);
            if (c != '0' && c != '1') return false;
        }
        return true;
    }
    
    public static boolean isValidHex(String hex){
        if (hex == null || hex.trim().isEmpty()) return false;
        hex = hex.trim().toUpperCase();
        boolean isNegative = hex.startsWith("-");
        if (isNegative) hex = hex.substring(1);
        if (hex.isEmpty()) return false;
        
        int dotIndex = hex.indexOf(".");
        String intPart = dotIndex >= 0 ? hex.substring(0, dotIndex) : hex;
        String fracPart = dotIndex >= 0 ? hex.substring(dotIndex + 1) : "";
        
        if (intPart.isEmpty() && fracPart.isEmpty()) return false;
        
        for (int i = 0; i < intPart.length(); i++){
            char c = intPart.charAt(i);
            if (!Character.isDigit(c) && (c < 'A' || c > 'F')) return false;
        }
        
        for (int i = 0; i < fracPart.length(); i++){
            char c = fracPart.charAt(i);
            if (!Character.isDigit(c) && (c < 'A' || c > 'F')) return false;
        }
        
        return true;
    }
    
    public static boolean isValidOctal(String octal){
        if (octal == null || octal.trim().isEmpty()) return false;
        octal = octal.trim();
        boolean isNegative = octal.startsWith("-");
        if(isNegative) octal = octal.substring(1);
        if(octal.isEmpty()) return false;
        
        int dotIndex = octal.indexOf(".");
        String intPart = dotIndex >= 0 ? octal.substring(0, dotIndex) : octal;
        String fracPart = dotIndex >= 0 ? octal.substring(dotIndex + 1) : "";
        
        if(intPart.isEmpty() && fracPart.isEmpty()) return false;
        
        for(int i = 0; i < intPart.length(); i++){
            char c = intPart.charAt(i);
            if (c < '0' || c > '7') return false;
        }
        
        for(int i = 0; i < fracPart.length(); i++){
            char c = fracPart.charAt(i);
            if (c < '0' || c > '7') return false;
        }
        
        return true;
    }
    
    public static boolean isValidIEEE754(String ieee754){
        if (ieee754 == null || ieee754.trim().isEmpty()) return false;
        ieee754 = ieee754.trim().replaceAll("\\s", "");
        if (ieee754.length() > 32) return false;
        for(int i = 0; i < ieee754.length(); i++){
            char c = ieee754.charAt(i);
            if (c != '0' && c != '1') return false;
        }
        
        return true;
    }
    
    public static String decimalToBinary(String decimal){
        try {
            double value = Double.parseDouble(decimal);
            if (value == 0) return "0"; 
            boolean isNegative = value < 0;
            value = Math.abs(value);
            int intPart = (int) value;
            double fracPart = value - intPart;
            StringBuilder binary = new StringBuilder();
            if (intPart == 0){
                binary.append("0");
            } 
            else{
                while (intPart > 0){
                    binary.insert(0, intPart % 2);
                    intPart /= 2;
                }
            }

            if (fracPart > 0) {
                binary.append(".");
                int precision = 20;
                while (fracPart > 0 && precision-- > 0) {
                    fracPart *= 2;
                    binary.append((int) fracPart);
                    fracPart -= (int) fracPart;
                }
            }
            return isNegative ? "-" + binary.toString() : binary.toString();
        } catch (NumberFormatException e) {
            return "Error";
        }
    }

    public static String binaryToDecimal(String binary){
        try {
            binary = binary.trim();
            boolean isNegative = binary.startsWith("-");
            if (isNegative) binary = binary.substring(1);
            
            int dotIndex = binary.indexOf(".");
            String intPart = dotIndex >= 0 ? binary.substring(0, dotIndex) : binary;
            String fracPart = dotIndex >= 0 ? binary.substring(dotIndex + 1) : "";

            long intValue = 0;
            long maxValueBeforeNeg = isNegative ? -MIN_INT_32 : MAX_INT_32;
            for(int i = 0; i < intPart.length(); i++){
                char c = intPart.charAt(i);
                if (c == '1') {
                    long power = (long) Math.pow(2, intPart.length() - 1 - i);

                    if (intValue > maxValueBeforeNeg - power) {
                        return "Error";
                    }
                    intValue += power;
                } else if (c != '0') {
                    return "Error";
                }
            }
            if (!isWithinInt32Range(isNegative ? -intValue : intValue)) {
                return "Error";
            }

            double fracValue = 0;
            for(int i = 0; i < fracPart.length(); i++){
                char c = fracPart.charAt(i);
                if (c == '1') {
                    fracValue += Math.pow(2, -(i + 1));
                } 
                else if (c != '0'){
                    return "Error";
                }
            }
            double result = intValue + fracValue;
            return isNegative ? "-" + String.valueOf(result) : String.valueOf(result);
        } 
        catch (Exception e){
            return "Error";
        }
    }
    
    public static String decimalToIEEE754(String decimal){
        try {
            float value = Float.parseFloat(decimal);
            int bits = Float.floatToIntBits(value);
            return String.format("%32s", Integer.toBinaryString(bits)).replace(' ', '0');
        } catch (NumberFormatException e) {
            return "Error";
        }
    }

    public static String ieee754ToDecimal(String ieee754){
        try {
            ieee754 = ieee754.trim().replaceAll("\\s", "");
            if (ieee754.length() != 32) {
                if(ieee754.length() < 32){
                    ieee754 = String.format("%32s", ieee754).replace(' ', '0');
                }
                else{
                    ieee754 = ieee754.substring(ieee754.length() - 32);
                }
            }
            int bits = Integer.parseUnsignedInt(ieee754, 2);
            float value = Float.intBitsToFloat(bits);
            if (Float.isNaN(value)) return "NaN";
            if (Float.isInfinite(value)) return value > 0 ? "Infinity" : "-Infinity";
            
            return String.valueOf(value);
        } catch (Exception e) {
            return "Error";
        }
    }

    public static String decimalToHex(String decimal){
        try {
            double value = Double.parseDouble(decimal);
            boolean isNegative = value < 0;
            value = Math.abs(value);
            
            int intPart = (int) value;
            double fracPart = value - intPart;
            
            StringBuilder hex = new StringBuilder();

            if (intPart == 0) {
                hex.append("0");
            } else {
                while (intPart > 0) {
                    int remainder = intPart % 16;
                    hex.insert(0, Integer.toHexString(remainder).toUpperCase());
                    intPart /= 16;
                }
            }

            if(fracPart > 0){
                hex.append(".");
                int precision = 10;
                while(fracPart > 0 && precision-- > 0){
                    fracPart *= 16;
                    int digit = (int) fracPart;
                    hex.append(Integer.toHexString(digit).toUpperCase());
                    fracPart -= digit;
                }
            }
            return isNegative ? "-" + hex.toString() : hex.toString();
        }catch (NumberFormatException e){
            return "Error";
        }
    }
    
    public static String hexToDecimal(String hex){
        try{
            hex = hex.trim().toUpperCase();
            boolean isNegative = hex.startsWith("-");
            if (isNegative) hex = hex.substring(1);
            
            int dotIndex = hex.indexOf(".");
            String intPart = dotIndex >= 0 ? hex.substring(0, dotIndex) : hex;
            String fracPart = dotIndex >= 0 ? hex.substring(dotIndex + 1) : "";
            long intValue = 0;
            long maxValueBeforeNeg = isNegative ? -MIN_INT_32 : MAX_INT_32;
            for(int i = 0; i < intPart.length(); i++){
                char c = intPart.charAt(i);
                int digit = Character.digit(c, 16);
                if(digit == -1) return "Error";
                if (intValue > (maxValueBeforeNeg - digit) / 16) {
                    return "Error";
                }
                intValue = intValue * 16 + digit;
            }
            if (!isWithinInt32Range(isNegative ? -intValue : intValue)) {
                return "Error";
            }
            
            double fracValue = 0;
            for(int i = 0; i < fracPart.length(); i++){
                char c = fracPart.charAt(i);
                int digit = Character.digit(c, 16);
                if (digit == -1) return "Error";
                fracValue += digit * Math.pow(16, -(i + 1));
            }
            double result = intValue + fracValue;
            return isNegative ? "-" + String.valueOf(result) : String.valueOf(result);
        }catch (Exception e){
            return "Error";
        }
    }

    public static String decimalToOctal(String decimal){
        try{
            double value = Double.parseDouble(decimal);
            boolean isNegative = value < 0;
            value = Math.abs(value);
            
            int intPart = (int) value;
            double fracPart = value - intPart;
            
            StringBuilder octal = new StringBuilder();

            if(intPart == 0){
                octal.append("0");
            } 
            else{
                while(intPart > 0){
                    octal.insert(0, intPart % 8);
                    intPart /= 8;
                }
            }
            if (fracPart > 0){
                octal.append(".");
                int precision = 15;
                while (fracPart > 0 && precision-- > 0){
                    fracPart *= 8;
                    octal.append((int) fracPart);
                    fracPart -= (int) fracPart;
                }
            }
            
            return isNegative ? "-" + octal.toString() : octal.toString();
        }catch(NumberFormatException e){
            return "Error";
        }
    }

    public static String octalToDecimal(String octal){
        try{
            octal = octal.trim();
            boolean isNegative = octal.startsWith("-");
            if (isNegative) octal = octal.substring(1);
            int dotIndex = octal.indexOf(".");
            String intPart = dotIndex >= 0 ? octal.substring(0, dotIndex) : octal;
            String fracPart = dotIndex >= 0 ? octal.substring(dotIndex + 1) : "";
            long intValue = 0;
            long maxValueBeforeNeg = isNegative ? -MIN_INT_32 : MAX_INT_32;
            for(int i = 0; i < intPart.length(); i++){
                char c = intPart.charAt(i);
                if (c < '0' || c > '7') return "Error";
                int digit = c - '0';
                if (intValue > (maxValueBeforeNeg - digit) / 8){
                    return "Error";
                }
                intValue = intValue * 8 + digit;
            }
            if(!isWithinInt32Range(isNegative ? -intValue : intValue)){
                return "Error";
            }
            
            double fracValue = 0;
            for (int i = 0; i < fracPart.length(); i++) {
                char c = fracPart.charAt(i);
                if (c < '0' || c > '7') return "Error";
                fracValue += (c - '0') * Math.pow(8, -(i + 1));
            }
            
            double result = intValue + fracValue;
            return isNegative ? "-" + String.valueOf(result) : String.valueOf(result);
        }
        catch (Exception e){
            return "Error";
        }
    }

    public static String decimalToTwosComplement(String decimal){
        try{
            double doubleValue = Double.parseDouble(decimal);
            if(!isWithinInt32Range(doubleValue)){
                return "Error";
            }
            
            int value = (int) doubleValue;
            if (value >= 0) {
                String binary = Integer.toBinaryString(value);
                return String.format("%32s", binary).replace(' ', '0');
            } 
            else{
                return Integer.toBinaryString(value);
            }
        } 
        catch (NumberFormatException e){
            return "Error";
        }
    }

    public static String twosComplementToDecimal(String twosComp){
        try {
            twosComp = twosComp.trim().replaceAll("\\s", "");
            if (twosComp.isEmpty()) return "Error";
            if (twosComp.length() < 32) {
                char signBit = twosComp.length() > 0 ? twosComp.charAt(0) : '0';
                twosComp = String.format("%32s", twosComp).replace(' ', signBit);
            } else if (twosComp.length() > 32) {
                twosComp = twosComp.substring(twosComp.length() - 32);
            }
            int value = (int) Long.parseLong(twosComp, 2);
            return String.valueOf(value);
        } 
        catch (Exception e){
            return "Error";
        }
    }
}

