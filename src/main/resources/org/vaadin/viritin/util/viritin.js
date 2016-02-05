
window.viritin = {
getChar: function(event) {
  if (event.which === null) {
    return String.fromCharCode(event.keyCode);
  } else if (event.which!==0 && event.charCode!==0) {
    return String.fromCharCode(event.which);
  }
  return null;
}
};


window.org_vaadin_viritin_util_HtmlElementPropertySetter = function() {
    
    var el = this.getElement(this.getParentId());
    
    this.xpathset = function(js, xpath, name, value) {
        if(js) {
            value = eval("("+value+")");
            name = "on" + name;
        }
        if(!xpath) {
            el[name] = value;
            return;
        }
        var r = document.evaluate(xpath, el, null, XPathResult.UNORDERED_NODE_ITERATOR_TYPE, null );
        var els = [];
        var targetEl;
        while(targetEl = r.iterateNext()) {
            els.push(targetEl);
        }
        var i;
        for(i = 0; i < els.length; i++) {
            var e = els[i];
            e[name] = value;
        }
    };

}


