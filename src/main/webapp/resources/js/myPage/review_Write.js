$(document).ready(function(){
		$("#img_file").change(function(){
			readURL(this);
		});
		function readURL(input) {
			if (input.files && input.files[0]) {
				var reader = new FileReader();
				reader.onload = function (e) {
					$('#ph').attr('src', e.target.result);
					$('#ph').css('visibility', 'visible');
					$('#ph').parent().parent().addClass('on');
				}                    
				reader.readAsDataURL(input.files[0]);
			}
		}
		$("#img_file2").change(function(){
			readURL2(this);
		});
		function readURL2(input) {
			if (input.files && input.files[0]) {
				var reader = new FileReader();
				reader.onload = function (e) {
					$('#ph2').attr('src', e.target.result);
					$('#ph2').css('visibility', 'visible');
					$('#ph2').parent().parent().addClass('on');
				}                    
				reader.readAsDataURL(input.files[0]);
			}
		}
		$("#img_file3").change(function(){
			readURL3(this);
		});
		function readURL3(input) {
			if (input.files && input.files[0]) {
				var reader = new FileReader();
				reader.onload = function (e) {
					$('#ph3').attr('src', e.target.result);
					$('#ph3').css('visibility', 'visible');
					$('#ph3').parent().parent().addClass('on');
				}                    
				reader.readAsDataURL(input.files[0]);
			}
		}
		$("#img_file4").change(function(){
			readURL4(this);
		});
		function readURL4(input) {
			if (input.files && input.files[0]) {
				var reader = new FileReader();
				reader.onload = function (e) {
					$('#ph4').attr('src', e.target.result);
					$('#ph4').css('visibility', 'visible');
					$('#ph4').parent().parent().addClass('on');
				}                    
				reader.readAsDataURL(input.files[0]);
			}
		}
	});

	$(function(){
		$('#content').keyup(function(){
		bytesHandler(this);
		});
	});
	
	function getTextLength(str) {
	var len = 0;
	
	for (var i = 0; i < str.length; i++) {
		if (escape(str.charAt(i)).length == 6) {
			len++;
			}
		len++;
		}
	return len;
	}
	
	function bytesHandler(obj){
	var text = $(obj).val();
	$('p.bytes').text(getTextLength(text) + "/ 500");
	if (getTextLength(text) > 500) {
		alert("?????? 500bytes?????? ?????? ???????????????.");
		//$(obj).val(text.substring(0, 50));
		}
	}
	
	//?????? ?????? ?????? ????????????????????? ??????
	function Rating(){};
	Rating.prototype.rate = 0;
	Rating.prototype.setRate = function(newrate){
	    //?????? ?????? - ????????? ??? ?????? ?????? ??? ?????? ??????
	    this.rate = newrate;
	    let items = document.querySelectorAll('.rate_radio');
	    items.forEach(function(item, idx){
	        if(idx < newrate){
	            item.checked = true;
	        }else{
	            item.checked = false;
	        }
	    });
	}
	let rating = new Rating();//?????? ???????????? ??????
	
	document.addEventListener('DOMContentLoaded', function(){
	    //???????????? ????????? ?????????
	    document.querySelector('.rating').addEventListener('click',function(e){
	        let elem = e.target;
	        if(elem.classList.contains('rate_radio')){
	            rating.setRate(parseInt(elem.value));
	        }
	        $("#point").val(parseInt(elem.value));
	    })
	});