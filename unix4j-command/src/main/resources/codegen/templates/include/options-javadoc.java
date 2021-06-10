 * <table>
 * <caption>Options</caption>
<#foreach opt in def.options?values>
 * <tr valign="top"><td style="width: 10px"></td><td style="white-space:nowrap">{@code -${opt.acronym}}</td><td>&nbsp;&nbsp;</td><td style="white-space:nowrap">{@code --${opt.name}}</td><td>&nbsp;</td><td>${opt.desc}</td></tr>
</#foreach>
 * </table>
