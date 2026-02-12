<%@ page language="java" contentType="text/html;charset=UTF-8"	session="true" isELIgnored="false" %>
<%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>MoSKito Control</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <META HTTP-EQUIV="EXPIRES" CONTENT="0">
    <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
    <META NAME="ROBOTS" CONTENT="NONE">
    <link rel="shortcut icon" href="../img/favicon.ico" type="image/x-icon">

    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../ext/bootstrap-3.3.7/css/bootstrap.css"/>
    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../ext/font-awesome-3.2.1/css/font-awesome.min.css">

    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../css/common.css" />

    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../ext/jquery.qtip2-3.0.3/jquery.qtip.min.css" />

    <link type="text/css" rel="stylesheet" rev="stylesheet" href="../css/qrcode.css" />

    <style>
        .custom-tooltip {
            position: relative;
            display: inline-block;
        }

        .custom-tooltip .tooltiptext {
            visibility: hidden;
            background-color: #f0f0f0;
            color: #333;
            border: 1px solid #ccc;
            text-align: center;
            padding: 4px 8px;
            border-radius: 4px;

            position: absolute;
            z-index: 1;
            bottom: 125%;
            left: 50%;
            transform: translateX(-50%);
            opacity: 0;
            transition: opacity 0.3s;
            font-size: 12px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
        }

        .custom-tooltip:hover .tooltiptext,
        .custom-tooltip.show .tooltiptext {
            visibility: visible;
            opacity: 1;
        }

        .custom-tooltip button {
            background-color: #007bff;
            color: #fff;
            border: none;
            padding: 6px 12px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
        }

        .custom-tooltip button:hover {
            background-color: #0056b3;
        }
    </style>

    <script>
        function copyToClipboard() {
            const text = document.getElementById("copyText").innerText;
            navigator.clipboard.writeText(text).then(() => {
                const tooltip = document.getElementById("tooltipText");
                const container = document.getElementById("tooltipContainer");

                container.classList.add("show");
                tooltip.textContent = "Copied!";

                setTimeout(() => {
                    container.classList.remove("show");
                }, 1500);
            });
        }
    </script>
</head>
<body>


<div class="wrapper">
    <div class="left-bar">

        <a href="main" class="logo">
            <img src="../img/logo.png" alt="MoSKito Control" border="0"/>
            <span class="version"><ano:write name="moskito.control.version"/></span>
        </a>

    </div>

    <div class="content">
        <div class="wrapper-content">
            <div class="box" >
                <h3> QR Code </h3>
                <p>Scan this QR Code to add the application to your MoSKito Control App.<br>
                    If you don't have the app yet, you can download it from the App Store or Google Play.<br>
                    <a href="https://apps.apple.com/app/moskito-monitoring/id6739428093?l=en-GB">Apple App Store</a>
                </p>
                <p>If you have the app downloaded and installed you can <button onclick="window.location.href='${qrCodeText}'">ADD CURRENT SYSTEM DIRECTLY</button> to the app.</p>
            </div>
            <div class="box mb-3">
                Alternatively you can paste the value direct into the application: <br>
                <p id="copyText">${qrCodeText}</p>
                <div class="custom-tooltip" id="tooltipContainer">
                    <button onclick="copyToClipboard()">Copy</button>
                    <span class="tooltiptext" id="tooltipText">Copied!</span>
                </div>
            </div>
            <div class="box">
                <img src="generateQRCode" alt="QR Code" style="width: 250px; height: auto;">
            </div>
        </div>
    </div>
</div>
<div at-magnifier-wrapper="">
    <div class="at-theme-light">
        <div class="at-base notranslate" translate="no">
            <div class="EuwGd" style="top: 0px; left: 0px;"></div>
        </div>
    </div>
</div>

</body>
</html>
