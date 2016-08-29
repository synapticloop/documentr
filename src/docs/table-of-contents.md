
## Generating the table of contents

`documentr` can automatically generate the table of contents for your documentation, 
simply by entering the following line into you `documentr.json` file:

```
{ "type": "markup", "value": "\\n\\n# Table of Contents\\n\\n" },

{ "type": "toc", "value": "2" },
{ "type": "toclinks", "value": "true" },
{ "type": "toplink", "value": " <a name=\"#documentr_top\"></a>" },
{ "type": "tocbacktotop", "value": " <sup><sup>[top](#documentr_top)</sup></sup>" },
```

### The table of contents title

By default, no title is generated - you will need to include one as simple markup.

### `{ "type": "toc" }`

This will generate the table of contents, and depending on any options, will generate 
links to the headers, and/or 'back to top links'.

The `{ "type": "toc", "value": "2" }` line will generate header links up to `h2` 
elements (i.e. both h1 and h2).

If the `value` attribute is omitted, then the default is to generate a table of 
contents for headers up to level `h6`.

### `{ "type": "toplink" }`

This is the anchor name to go back to the top of the README file (rather than 
the top of the page.  By default this is always set to `<a name=\"#documentr_top\"></a>` 
if you wish to change the `tocbacktotop`, then you will need to change this as 
well.

### `{ "type": "toclinks" }`

This is a post processing command and will generate links to the headers in the 
page.  This option is only invoked if there is a table of contents generated 
(i.e. . `{ "type": "toc", "value": "2" }`)

### `{ "type": "tocbacktotop" }`

This is a post processing command and will generate 'back to top' links for all 
of the included headers.

The line: `{ "type": "tocbacktotop", value: " <sup><sup>[top](#documentr_top)</sup></sup>"}` 
will generate a link after every heading (up to the table of content level number 
set above).

The value is the HTML/Markdown that is appended to the heading.

If you change the anchor link from `#documentr_top`, you *MUST* also include and 
update the `toplink` entry above.

### Important note:

> Unfortunately the developer of the markdown  processor that is in use, does not distinguish block quote level elements correctly such that anything that looks like a header included in a blockquote will also be  incorrectly identified as a header.  However, `documentr` will attempt to pre-process the code fence blocks and remove them before parsing.


