<%@ page language="java" contentType="text/html;charset=UTF-8" session="true"
        %><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano"
        %>
<ano:iterate id="accumulator" name="accumulators">
    <p><ano:write name="accumulator"/></p>
</ano:iterate>
