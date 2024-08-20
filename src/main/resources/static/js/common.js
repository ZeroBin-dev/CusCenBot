function postApi(url, params, successCallback, errorCallback) {
  $.ajax({
    type: 'POST',
    url: url,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    },
    data: JSON.stringify(params),
    success: function (data) {
      if (typeof successCallback === 'function') {
        successCallback(data);
      }
    },
    error: function (err) {
      if (typeof errorCallback === 'function') {
        errorCallback(err);
      }
    }
  });
}

function onApiError(err) {
  closeModal();
  alert(err);
}

function openModal() {
  document.querySelector('.modal').style.display = 'block';
}

// 모달 닫기
function closeModal() {
  document.querySelector('.modal').style.display = 'none';
}
