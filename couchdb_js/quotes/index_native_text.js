// index_native_text
function(doc) {
    var idx, key;
    if (doc.text && doc.text.scriptRenderings) {
        for (idx in doc.text.scriptRenderings) {
            key = doc.text.scriptRenderings[idx].text;
            emit(key, 1);
        }
    }
}