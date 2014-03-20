var utils = {
	monthNames: [
         "January",
         "February",
         "March",
         "April",
         "May",
         "June",
         "July",
         "August",
         "September",
         "October",
         "November",
         "December"
	],
	
	formatDate: function(d, format) {
		
		var month = d.getMonth()+1;
		var day = d.getDate();
		var year = d.getFullYear();
		
		var dayText = day.toString();
		var yearText = year.toString();
		
		if (dayText.length == 1)
			dayText = "0" + dayText;
		
		if (format == null || format == "" || format == "short") {
			var monthText = month.toString();
			
			if (monthText.length == 1)
				monthText = "0" + monthText;
			
			return monthText + "/" + dayText + "/" + yearText;
		} else if (format == "long") {
			var monthText = utils.monthNames[d.getMonth()];
			return monthText + " " + dayText + ", " + yearText;
		}
	},
	
	formatTime: function(d) {
		var hours = d.getHours();
		var minutes = d.getMinutes();
		var period = "AM";
		
		if (hours >= 12)
			period = "PM";
		
		if (hours > 12)
			hours -= 12;
		
		var hoursText = hours.toString();
		var minutesText = minutes.toString();
		
		if (hoursText.length == 1)
			hoursText = "0" + hoursText;
		if (minutesText.length == 1)
			minutesText = "0" + minutesText;
		
		return hoursText + ":" + minutesText + " " + period;
	},
	
	formatTimestamp: function(d, format) {
		return utils.formatDate(d, format) + " " + utils.formatTime(d);
	},
	
	isNull: function(val) {
		return val == null || val == "" || val == "null" || typeof val == "undefined";
	}
};

var JSON = {
    cx: null,
    gap: null,
    indent: null,
    rep: null,
	
    escapable: /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
    
    meta: {    // table of character substitutions
        '\b': '\\b',
        '\t': '\\t',
        '\n': '\\n',
        '\f': '\\f',
        '\r': '\\r',
        '"' : '\\"',
        '\\': '\\\\'
    },

	quote: function(string) {
		
		// If the string contains no control characters, no quote characters, and no
		// backslash characters, then we can safely slap some quotes around it.
		// Otherwise we must also replace the offending characters with safe escape
		// sequences.

        JSON.escapable.lastIndex = 0;
        return JSON.escapable.test(string) ? '"' + string.replace(JSON.escapable, function (a) {
            var c = JSON.meta[a];
            return typeof c === 'string'
                ? c
                : '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
        }) + '"' : '"' + string + '"';
    },

    str: function(key, holder) {

    	// Produce a string from holder[key].

        var i,          // The loop counter.
            k,          // The member key.
            v,          // The member value.
            length,
            mind = JSON.gap,
            partial,
            value = holder[key];

        // If the value has a toJSON method, call it to obtain a replacement value.

        if (value && typeof value === 'object' &&
                typeof value.toJSON === 'function') {
            value = value.toJSON(key);
        }

		// If we were called with a replacer function, then call the replacer to
		// obtain a replacement value.

        if (typeof JSON.rep === 'function') {
            value = JSON.rep.call(holder, key, value);
        }

        // What happens next depends on the value's type.

        switch (typeof value) {
        case 'string':
            return JSON.quote(value);

        case 'number':

        	// JSON numbers must be finite. Encode non-finite numbers as null.

            return isFinite(value) ? String(value) : 'null';

        case 'boolean':
        case 'null':

			// If the value is a boolean or null, convert it to a string. Note:
			// typeof null does not produce 'null'. The case is included here in
			// the remote chance that this gets fixed someday.

            return String(value);

		// If the type is 'object', we might be dealing with an object or an array or
		// null.

        case 'object':

			// Due to a specification blunder in ECMAScript, typeof null is 'object',
			// so watch out for that case.

            if (!value) {
                return 'null';
            }

            // Make an array to hold the partial results of stringifying this object value.

            JSON.gap += JSON.indent;
            partial = [];

            // Is the value an array?

            if (Object.prototype.toString.apply(value) === '[object Array]') {

				// The value is an array. Stringify every element. Use null as a placeholder
				// for non-JSON values.

                length = value.length;
                for (i = 0; i < length; i += 1) {
                    partial[i] = JSON.str(i, value) || 'null';
                }

				// Join all of the elements together, separated with commas, and wrap them in
				// brackets.

                v = partial.length === 0
                    ? '[]'
                    : JSON.gap
                    ? '[\n' + JSON.gap + partial.join(',\n' + JSON.gap) + '\n' + mind + ']'
                    : '[' + partial.join(',') + ']';
                JSON.gap = mind;
                return v;
            }

            // If the replacer is an array, use it to select the members to be stringified.

            if (JSON.rep && typeof JSON.rep === 'object') {
                length = JSON.rep.length;
                for (i = 0; i < length; i += 1) {
                    if (typeof JSON.rep[i] === 'string') {
                        k = JSON.rep[i];
                        v = JSON.str(k, value);
                        if (v) {
                            partial.push(JSON.quote(k) + (JSON.gap ? ': ' : ':') + v);
                        }
                    }
                }
            } else {

            	// Otherwise, iterate through all of the keys in the object.

                for (k in value) {
                    if (Object.prototype.hasOwnProperty.call(value, k)) {
                        v = JSON.str(k, value);
                        if (v) {
                            partial.push(JSON.quote(k) + (JSON.gap ? ': ' : ':') + v);
                        }
                    }
                }
            }

			// Join all of the member texts together, separated with commas,
			// and wrap them in braces.

            v = partial.length === 0
                ? '{}'
                : JSON.gap
                ? '{\n' + JSON.gap + partial.join(',\n' + JSON.gap) + '\n' + mind + '}'
                : '{' + partial.join(',') + '}';
            JSON.gap = mind;
            return v;
        }
    },

    stringify: function(value, replacer, space) {

		// The stringify method takes a value and an optional replacer, and an optional
		// space parameter, and returns a JSON text. The replacer can be a function
		// that can replace values, or an array of strings that will select the keys.
		// A default replacer method can be provided. Use of the space parameter can
		// produce text that is more easily readable.

        var i;
        JSON.gap = '';
        JSON.indent = '';

		// If the space parameter is a number, make an indent string containing that
		// many spaces.

        if (typeof space === 'number') {
            for (i = 0; i < space; i += 1) {
                JSON.indent += ' ';
            }

            // If the space parameter is a string, it will be used as the indent string.

        } else if (typeof space === 'string') {
            JSON.indent = space;
        }

		// If there is a replacer, it must be a function or an array.
		// Otherwise, throw an error.

        JSON.rep = replacer;
        if (replacer && typeof replacer !== 'function' &&
                (typeof replacer !== 'object' ||
                typeof replacer.length !== 'number')) {
            throw new Error('JSON.stringify');
        }

		// Make a fake root object containing our value under the key of ''.
		// Return the result of stringifying the value.

        return JSON.str('', {'': value});
    },
    
    parse: function(text, reviver) {

		// The parse method takes a text and an optional reviver function, and returns
		// a JavaScript value if the text is a valid JSON text.

        var j;

        function walk(holder, key) {

			// The walk method is used to recursively walk the resulting structure so
			// that modifications can be made.

            var k, v, value = holder[key];
            if (value && typeof value === 'object') {
                for (k in value) {
                    if (Object.prototype.hasOwnProperty.call(value, k)) {
                        v = walk(value, k);
                        if (v !== undefined) {
                            value[k] = v;
                        } else {
                            delete value[k];
                        }
                    }
                }
            }
            return reviver.call(holder, key, value);
        }


		// Parsing happens in four stages. In the first stage, we replace certain
		// Unicode characters with escape sequences. JavaScript handles many characters
		// incorrectly, either silently deleting them, or treating them as line endings.

        text = String(text);
        JSON.cx.lastIndex = 0;
        if (JSON.cx.test(text)) {
            text = text.replace(JSON.cx, function (a) {
                return '\\u' +
                    ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
            });
        }

		// In the second stage, we run the text against regular expressions that look
		// for non-JSON patterns. We are especially concerned with '()' and 'new'
		// because they can cause invocation, and '=' because it can cause mutation.
		// But just to be safe, we want to reject all unexpected forms.
		
		// We split the second stage into 4 regexp operations in order to work around
		// crippling inefficiencies in IE's and Safari's regexp engines. First we
		// replace the JSON backslash pairs with '@' (a non-JSON character). Second, we
		// replace all simple value tokens with ']' characters. Third, we delete all
		// open brackets that follow a colon or comma or that begin the text. Finally,
		// we look to see that the remaining characters are only whitespace or ']' or
		// ',' or ':' or '{' or '}'. If that is so, then the text is safe for eval.

        if (/^[\],:{}\s]*$/
                .test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@')
                    .replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']')
                    .replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {

			// In the third stage we use the eval function to compile the text into a
			// JavaScript structure. The '{' operator is subject to a syntactic ambiguity
			// in JavaScript: it can begin a block or an object literal. We wrap the text
			// in parens to eliminate the ambiguity.

            j = eval('(' + text + ')');

			// In the optional fourth stage, we recursively walk the new structure, passing
			// each name/value pair to a reviver function for possible transformation.

            return typeof reviver === 'function'
                ? walk({'': j}, '')
                : j;
        }

        // If the text is not JSON parseable, then a SyntaxError is thrown.

        throw new SyntaxError('JSON.parse');
    }
};
