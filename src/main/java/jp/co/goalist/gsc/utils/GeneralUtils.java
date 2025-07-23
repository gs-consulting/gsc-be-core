package jp.co.goalist.gsc.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.enums.ClientScreenPermission;
import jp.co.goalist.gsc.enums.ScreenPermission;
import jp.co.goalist.gsc.exceptions.BadValidationException;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static jp.co.goalist.gsc.common.Constants.*;

@Slf4j
public class GeneralUtils {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public static String toJson(Object object) {
        String result;
        try {
            result = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Can not serialize object", e);
            result = "";
        }

        return result;
    }

    public static <T> List<T> fromJsonToList(String json, Class<T> elementClazz) throws Exception {
        try {
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, elementClazz));
        } catch (JsonProcessingException e) {
            log.error("Can not deserialize list", e);
            throw e;
        }
    }

    /**
     * This function is not optimized for performance, use it only when necessary <p>
     * <p>
     * It will be deprecated in the future
     *
     * @return the current user
     */
    public static Account getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Account) authentication.getPrincipal();
    }

    public static String convertBase64ToString(String encoded) {
        return new String(Base64.getDecoder().decode(encoded));
    }

    public static String wrapToLike(String value) {
        return Objects.isNull(value) || value.isBlank() ? null : "%" + value + "%";
    }

    public static String decodeString(String encodedStr) {
        return new String(Base64.getUrlDecoder().decode(encodedStr), StandardCharsets.UTF_8);
    }

    public static String randomTokenString() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('A', 'Z')
                .withinRange('0', '9')
                .withinRange('a', 'z')
                .get();

        return generator.generate(TOKEN_LENGTH);
    }

    public static Pageable getPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable;
        if (Objects.isNull(pageNumber) || Objects.isNull(pageSize)) {
            pageable = Pageable.unpaged();
        } else {
            pageable = PageRequest.of(pageNumber - 1, pageSize);
        }
        return pageable;
    }

    public static Pageable getPagination(Integer pageNumber, Integer pageSize, Sort sort) {
        Pageable pageable;

        if (Objects.isNull(pageNumber) || Objects.isNull(pageSize)) {
            pageable = Pageable.unpaged(sort);
        } else {
            pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        }

        return pageable;
    }

    public static void validatePermissions(List<String> permissions) {
        if (permissions != null && !permissions.isEmpty()) {
            for (String permission : permissions) {
                // check not in PermissionScreen
                if (!ScreenPermission.getScreenPermissions().contains(permission)) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                            .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                                    "permissions",
                                    permission))
                            .build());
                }
            }
        }
    }

    public static void validateClientPermissions(List<String> permissions, String fieldName) {
        if (permissions != null && !permissions.isEmpty()) {
            for (String permission : permissions) {
                // check not in PermissionScreen
                if (!ClientScreenPermission.getPermissions().contains(permission)) {
                    throw new BadValidationException(ErrorResponse.builder()
                            .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                            .message(String.format(ErrorMessage.INVALID_DATA.getMessage(),
                                    fieldName,
                                    permission))
                            .build());
                }
            }
        }
    }


    public static void validateEmailChange(String currentOne, String updateOne) {
        if (!Objects.equals(currentOne, updateOne)) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.EMAIL_CHANGED.getStatusCode())
                    .message(ErrorMessage.EMAIL_CHANGED.getMessage())
                    .fieldError("email")
                    .build());
        }
    }

    /**
     * To store an object to request attribute with SCOPE_REQUEST
     *
     * @param <T>   dynamic type T
     * @param key   string
     * @param value T object
     */
    public static <T> void setToRequestAttribute(String key, T value) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            attributes.setAttribute(key, value, ServletRequestAttributes.SCOPE_REQUEST);
        }
    }

    /**
     * To get an object from request attribute with SCOPE_REQUEST
     *
     * @param <T> dynamic type T
     * @param key string
     * @return T object
     */
    public static <T> T getFromRequestAttribute(String key) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return (T) attributes.getAttribute(key, ServletRequestAttributes.SCOPE_REQUEST);
        }
        return null;
    }

    private static boolean validateDateTimeRange(LocalDateTime datetime1, LocalDateTime datetime2) {
        try {
            return datetime1.isAfter(datetime2);
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }

    public static void validateDateTimeRangeFields(LocalDateTime startDateTime, LocalDateTime endDateTime, String field, String fieldError) {
        if (Objects.nonNull(startDateTime) && Objects.nonNull(startDateTime) && validateDateTimeRange(startDateTime, endDateTime)) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message(String.format(ErrorMessage.INVALID_DATA.getMessage(), field))
                    .fieldError(fieldError)
                    .build());
        }
    }

    /*** Criteria Builder ***/
    public static String escapeSpecialCharacter(String value) {
        return value.replace("_", "\\_")
                .replace("%", "\\%")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("{", "\\{")
                .replace("}", "\\}");
    }

    public static String wrapToLikeWithSpecialCharacter(String value) {
        return Objects.isNull(value) || value.isBlank() ? null : "%" + escapeSpecialCharacter(value) + "%";
    }

    public static String createInStatement(String field, String parameter) {
        return String.format("%s in (%s)", field, parameter);
    }

    public static String wrappedStatement(String query, String operator) {
        return " " + operator + " (" + query + ")";
    }

    public static String likeStatement(String field, String kw, String escape) {
        return String.format("%s like '%s' escape '%s'", field, wrapToLikeWithSpecialCharacter(kw), escape);
    }

    public static String likeStatementWithoutWildcard (String field, String kw, String escape) {
        return String.format("%s like %s escape '%s'", field, kw, escape);
    }

    public static String equalStatement(String field, String parameter) {
        return String.format("%s = '%s'", field, parameter);
    }

    public static String createInStatement(String field, Set<String> values) {
        boolean isNotEnter = false;
        if (values.contains(SEARCH_NOT_ENTER)) {
            values.remove(SEARCH_NOT_ENTER);
            isNotEnter = true;

            if (values.isEmpty()) {
                return String.format("%s IS NULL", field);
            }
        }

        StringBuilder parameter = new StringBuilder();
        boolean isFirst = true;
        for (String v : values) {
            if (isFirst) {
                parameter.append("'").append(v).append("'");
            } else {
                parameter.append(", '").append(v).append("'");
            }
            isFirst = false;
        }
        String inStatement = createInStatement(field, parameter.toString());

        if (isNotEnter) {
            return String.format("(%s IS NULL) OR (%s)", field, inStatement);
        }

        return inStatement;
    }

    public static String createUnionStatement(String statement1, String statement2) {
        return String.format("(%s) UNION ALL (%s)",
                statement1,
                statement2);
    }

    public static String createFindInSetStatement(String field, Set<String> values, String operator) {
        boolean isNotEnter = false;
        if (values.contains(SEARCH_NOT_ENTER)) {
            values.remove(SEARCH_NOT_ENTER);
            isNotEnter = true;

            if (values.isEmpty()) {
                return String.format("%s IS NULL", field);
            }
        }

        StringBuilder queryBuilder = new StringBuilder();
        boolean isNext = false;
        for (String v : values) {
            if (isNext) {
                queryBuilder.append(operator);
            }
            queryBuilder.append(String.format("FIND_IN_SET('%s',%s)>%d", v, field, 0));
            isNext = true;
        }

        if (isNotEnter) {
            return String.format("(%s IS NULL) OR (%s)", field, queryBuilder);
        }

        return queryBuilder.toString();
    }

    public static LocalDateTime parseDateTime(String str, String field, DateTimeFormatter formatter) {
        try {
            return LocalDateTime.parse(str, formatter);
        } catch (Exception e) {
            throw new BadValidationException(ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message(ErrorMessage.INVALID_DATA.getMessage())
                    .fieldError(field)
                    .build());
        }
    }

    public static boolean hasPermission(ScreenPermission permission) {
        String permissionStr = GeneralUtils.getFromRequestAttribute("permissions");
        if (Objects.nonNull(permissionStr) && !permissionStr.isEmpty()) {
            List<String> permissions = List.of(permissionStr.split(","));
            return permissions.contains(permission.getId());
        }

        return false;
    }

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static float round(float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }
}
