// 导出的流Blob,filename 导出的文件名
export function downloadFileBlob(data,filename) {
    if (!data) {
          return
      }
      let url = window.URL.createObjectURL(new Blob([data]))
      let link = document.createElement('a')
      link.style.display = 'none'
      link.href = url
      link.setAttribute('download', filename)
  
      document.body.appendChild(link)
      link.click()
  }
  // 用a链接导出处理方案GET方式
  export function downloadALink(url,filename){
    var a = document.createElement('a');
    a.setAttribute('href', url);
    a.setAttribute('target', '_blank');
    a.setAttribute('download', filename);
    a.setAttribute('id', 'startTelMedicine');
    // 防止反复添加
    if(document.getElementById('startTelMedicine')) {
      document.body.removeChild(document.getElementById('startTelMedicine'));
    }
    document.body.appendChild(a);
    a.click();
  }
  