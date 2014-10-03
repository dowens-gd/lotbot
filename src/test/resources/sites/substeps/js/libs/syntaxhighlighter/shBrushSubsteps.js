/**
 * SyntaxHighlighter
 * http://alexgorbatchev.com/SyntaxHighlighter
 *
 * SyntaxHighlighter is donationware. If you are using it, please donate.
 * http://alexgorbatchev.com/SyntaxHighlighter/donate.html
 *
 * @version
 * 3.0.83 (July 02 2010)
 * 
 * @copyright
 * Copyright (C) 2004-2010 Alex Gorbatchev.
 *
 * @license
 * Dual licensed under the MIT and GPL licenses.
 */
;(function()
{
	// CommonJS
	typeof(require) != 'undefined' ? SyntaxHighlighter = require('shCore').SyntaxHighlighter : null;

	function Brush()
	{
		function getKeywordsCSS(str)
		{
			return '\\b([a-z_]|)' + str.replace(/ /g, '(?=:)\\b|\\b([a-z_\\*]|\\*|)') + '(?=:)\\b';
		};

		var keywords =	'Scenario Outline Define Background Examples';

		var keywords2 = 'Scenario';

		var feature =	'Feature';

		var tags = 'Tags';
	
		this.regexList = [
			{ regex: /\s*#.*/gm,										css: 'value' },			// one line comments
			{ regex: new RegExp(getKeywordsCSS(keywords), 'gm'),		css: 'string' },		// keywords
			{ regex: new RegExp(this.getKeywords(keywords), 'gm'),		css: 'string'},			// catch Scenario Outline
			{ regex: new RegExp(getKeywordsCSS(feature), 'gm'),			css: 'color3' },		// functions
			{ regex: new RegExp(getKeywordsCSS(tags), 'gm'),			css: 'preprocessor' }	// tags
			];
	};

	Brush.prototype	= new SyntaxHighlighter.Highlighter();
	Brush.aliases	= ['substeps'];

	SyntaxHighlighter.brushes.Substeps = Brush;

	// CommonJS
	typeof(exports) != 'undefined' ? exports.Brush = Brush : null;
})();
