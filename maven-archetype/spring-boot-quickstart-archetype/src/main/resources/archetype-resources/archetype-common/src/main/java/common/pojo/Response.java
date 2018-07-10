#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.pojo;

/**
 * <br>created at 18-4-19
 *
 * @author liuxh
 * @since 0.1.0
 */
public class Response<T> {
    private Header header;
    private T body;

    public Header getHeader() {
        return header;
    }

    public Response<T> setHeader(Header header) {
        this.header = header;
        return this;
    }

    public T getBody() {
        return body;
    }

    public Response<T> setBody(T body) {
        this.body = body;
        return this;
    }

    public static class Header {
        private Integer code;
        private String desc;
        private String uri;

        public Integer getCode() {
            return code;
        }

        public Header setCode(Integer code) {
            this.code = code;
            return this;
        }

        public String getDesc() {
            return desc;
        }

        public Header setDesc(String desc) {
            this.desc = desc;
            return this;
        }

        public String getUri() {
            return uri;
        }

        public Header setUri(String uri) {
            this.uri = uri;
            return this;
        }
    }
}
