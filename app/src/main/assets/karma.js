// in K array
// positive = 0
// negative = 1
// vote = 2
// ovner = 3

function ka_kick()
{
	var as=document.getElementsByTagName('a');
	for(var i=0;i<as.length;i++)
	{
		if(as[i].getAttribute('rel')=='prev')
		{
			try{as[i].click();}catch(e){}
			try{window.location.href=as[i].getAttribute('href');}catch(e){}
			break;
		}
	}
}

function ka_ih(p,c)
{
	var d=ka[p][c];
	if(d[1]>20&&d[0]*3<d[1]*2)return true;
	return false;
}

function ka_nexist(o)
{
	if(o&&o!='undefined'&&o!=null)return false;
	return true;
}

function ka_vote(p,c,v)
{
	var req,url,o,d,e,n;
	if(ka_nexist(ka[p][c]))return;
	if(ka_nexist(e=document.getElementById('ka_'+p+'_'+c)))return;
	try{req=new XMLHttpRequest();}catch(e){}
	if(ka_nexist(req))try{req=new ActiveXObject('Microsoft.XMLHTTP');}catch(e){}
	if(ka_nexist(req)){alert("Can't create AJAX object.");return false;}
	e.innerHTML='Ñïàñèáî';
	if(!ka_nexist(n=document.getElementById('ka_'+p+'_'+c+'_n')))n.innerHTML='';
	d=ka[p][c];
	url='/wp-content/plugins/karma/ajax.php?p='+p+'&c='+c+'&v='+v;
	req.onreadystatechange=function(){
		if(req.readyState!= 4)return;
		if(req.status!=200)return;
		if(11>ka_u)d[2]=v;
		if(10<ka_u){if(v>0)d[0]+=v;else d[1]+=Math.abs(v);}else{if(v>0)d[0]++;else d[1]++;}
		if(10<ka_u)ka_draw(p,c);else window.setTimeout('ka_draw(\''+p+'\','+c+');'+((0>v&&c==0)?'ka_kick();':''),500);
	};
	req.open('GET',url,true);
	req.send(null);
	return false;
}

function ka_a_pos(p,c)
{
	if(ka_nexist(ka[p][c]))return;
	var r=prompt('Ââåäèòå êîëè÷åñòâî ïîçèòèâíûõ ãîëîñîâ:');
	if(!r)return;
	r=Math.abs(parseInt(r));
	if(isNaN(r)){alert('Ââåäåíî íå ÷èñëî! Ïîâòîðèòå ïîïûòêó.');return;}
	ka_vote(p,c,r);
}

function ka_pos(p,c)
{
	ka_vote(p,c,1);
}

function ka_a_neg(p,c)
{
	if(ka_nexist(ka[p][c]))return;
	var r=prompt('Ââåäèòå êîëè÷åñòâî íåãàòèâíûõ ãîëîñîâ:');
	if(!r)return;
	r=Math.abs(parseInt(r));
	if(isNaN(r)){alert('Ââåäåíî íå ÷èñëî! Ïîâòîðèòå ïîïûòêó.');return;}
	ka_vote(p,c,-r);
}

function ka_neg(p,c)
{
	if(ka_nexist(ka[p][c]))return;
	ka_vote(p,c,-1);
}

function ka_hist(p,c)
{
	window.open('/wp-content/plugins/karma/ajax.php?p='+p+'&c='+c+'&v=0','ka_hist');
}

function ka_hide(p,c)
{
	var e;
	if(!ka_nexist(e=document.getElementById('ka_attr_'+p+'_'+c)))e.className+=' ka-hide';
	if(!ka_nexist(e=document.getElementById('ka_attr2_'+p+'_'+c)))e.className+=' ka-hide';
	if(!ka_nexist(e=document.getElementById('ka_cont_'+p+'_'+c)))e.style.display='none';
	if(!ka_nexist(e=document.getElementById('ka_meta_'+p+'_'+c)))e.style.display='none';
	if(!ka_nexist(e=document.getElementById('ka_btn_'+p+'_'+c)))e.innerHTML='<span class="vote_n spr ph" onclick="ka_show('+p+','+c+');" title="Ðàçâåðíóòü"></span>';
	if(!ka_nexist(e=document.getElementById('ka_'+p+'_'+c+'_n')))e.style.display='none';
	if(c!=0&&!ka_nexist(e=document.getElementById('ka_'+p+'_'+c)))e.style.display='none';
}

function ka_show(p,c)
{
	var e;
	if(!ka_nexist(e=document.getElementById('ka_cont_'+p+'_'+c)))e.style.display='';
	if(!ka_nexist(e=document.getElementById('ka_meta_'+p+'_'+c)))e.style.display='';
	if(!ka_nexist(e=document.getElementById('ka_'+p+'_'+c+'_n')))e.style.display='';
	if(!ka_nexist(e=document.getElementById('ka_'+p+'_'+c)))e.style.display='';
	if(!ka_nexist(e=document.getElementById('ka_btn_'+p+'_'+c)))e.innerHTML=(ka[p][c][2]!=0||11>ka_u||ka[p][c][3])?'<span class="vote_n spr pm" onclick="ka_hide('+p+','+c+');" title="Ñâåðíóòü"></span>':(10<ka_u)?'':'<span class="vote_n spr pm" title="'+(ka_u>10?'Ìèíóñ':'Ñâåðíóòü')+'" onclick="ka_neg(\''+p+'\','+c+');"></span>';
	return '';
}

function ka_draw(p,c)
{
	var e,d,e2,h;
	if(ka_nexist(ka)||ka_nexist(ka[p])||ka_nexist(ka[p][c])||ka_nexist(e=document.getElementById('ka_'+p+'_'+c)))return;
	d=ka[p][c];
	e2=null;
	if(d[2]==-1||(c!=0&&ka_ih(p,c))) // ñâîðà÷èâàåì, è â ñëó÷àå çàìèíóñîâûâàíèÿ
	{
		ka_hide(p,c);
		if(c!=0&&!ka_nexist(e2=document.getElementById('ka_meta_'+p+'_'+c)))e2.innerHTML='';
	}
	e.innerHTML=(
	(
		( d[2]==0 && !d[3] && ka_u > -10 && ka[p][0][2] != -1 )
		|| 10 < ka_u
	) ?
		'<span class="vote"><span class="vote_p spr pp" title="Ìíå íðàâèòñÿ" onclick="ka_pos(\'' + p + '\',' + c + ');"></span><span class="vote_v"' + (
		10 < ka_u ?
			' onclick="ka_hist(\'' + p + '\',' + c + ')"'
			:
			''
		) + '>' + d[0] + (
		10 < ka_u ?
			' | ' + d[1]
			:
			''
		) + '</span></span>'
		:
		'Ïîíðàâèëîñü: <span class="vote_v">' + d[0] + '</span>'
	);
/*
	if ( ka_u > 10 ){
		e.innerHTML+='<span class="vote_p" title="+N ãîëîñîâ" style="color:#FFF;font-weight:bold;padding:1px;" onclick="ka_a_pos(\'' + p + '\',' + c + ');">&#8224;</span>';
	}
*/
	e=document.getElementById('ka_'+p+'_'+c+'_n');
	if(ka_nexist(e))return;
	e.innerHTML='';
	e.innerHTML+=(
	(
		( d[2] == 0 && !d[3] && ka_u > -10 && ka_nexist ( e2 ) )
		|| 10 < ka_u
	) ?
		(
		( ka[p][0][2] != -1 ) ?
			'<span class="vote_n spr pm" title="' + (
			ka_u > 10 ?
				'Ìèíóñ'
				:
				'Ñâåðíóòü'
			) + '" onclick="ka_neg(\'' + p + '\',' + c + ');"></span>'
			:
			ka_show(p,c)
		)
		:
		''
	);
	if ( ka_u > 10 ){
		e.innerHTML+='<span class="vote_n" title="-N ãîëîñîâ" style="color:#FFF;font-weight:bold;position:relative;" onclick="ka_a_neg(\'' + p + '\',' + c + ');">&nbsp;*&nbsp;</span>';
	}
}

function ka_gfc(p)
{
	if(typeof $s=='undefined'||ka_nexist(commentformid)||ka_nexist(ka[p][0])||ka_nexist(ka[p][0][2])||ka[p][0][2]!=-1)return false;
	var cfm=$s(commentformid);
	if(ka_nexist(cfm))return false;
	var fi=cfm.getElementsByTagName('input');
	for(var i=0;i<fi.length;i++)if(fi[i].name=='comment_post_ID')if(fi[i].value==p){cfm.style.display='none';return true;}
	return false;
}

function ka_draw_post(p)
{
	var e,d;
	if(ka_nexist(ka)||ka_nexist(ka[p]))return;
	var hide=ka_gfc(p);
	for(var c in ka[p])
	{
		if(ka_nexist(ka[p][c]))continue;
		if(hide&&!ka_nexist(e=document.getElementById('ka_meta_'+p+'_'+c)))e.innerHTML='';
		ka_draw(p,c);
	}
}

// initialisation here
if('undefined'!=typeof(ka))for(p in ka)ka_draw_post(p);
