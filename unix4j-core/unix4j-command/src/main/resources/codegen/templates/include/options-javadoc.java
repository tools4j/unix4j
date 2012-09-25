 * <table>
<#foreach opt in def.options?values>
 * <tr valign="top"><td width="10px"></td><td nowrap="nowrap">{@code -${opt.acronym}}</td><td>&nbsp;&nbsp;</td><td nowrap="nowrap">{@code --${opt.name}}</td><td>&nbsp;</td><td>${opt.desc}</td></tr>
</#foreach>
 * </table>
