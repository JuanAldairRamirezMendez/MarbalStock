<#
  Script: run-tests-with-junit.ps1
  Qué hace:
  - Descarga `junit-platform-console-standalone` si no existe en `lib/`
  - Compila fuentes y tests colocando el JAR en el classpath
  - Ejecuta el JAR standalone para descubrir y correr tests en `out/classes`
#>

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
$libDir = Join-Path $projectRoot 'lib'
New-Item -ItemType Directory -Path $libDir -Force | Out-Null

$junitVersion = '1.9.2'
$junitJar = "junit-platform-console-standalone-$junitVersion.jar"
$junitUrl = "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/$junitVersion/$junitJar"
$junitPath = Join-Path $libDir $junitJar

if (-not (Test-Path $junitPath)) {
    Write-Host "Descargando $junitJar..."
    Invoke-WebRequest -Uri $junitUrl -OutFile $junitPath -UseBasicParsing
    if ($LASTEXITCODE -ne 0) { Write-Error "Fallo al descargar $junitJar"; exit 3 }
}

$srcDir = Join-Path $projectRoot 'src'
$outDir = Join-Path $projectRoot 'out\classes'
if (Test-Path $outDir) { Remove-Item $outDir -Recurse -Force }
New-Item -ItemType Directory -Path $outDir -Force | Out-Null

$javaFiles = Get-ChildItem -Path $srcDir -Recurse -Filter *.java | ForEach-Object { $_.FullName }
if ($javaFiles.Count -eq 0) { Write-Error "No hay archivos .java bajo $srcDir"; exit 2 }

Write-Host "Compilando fuentes y tests (incluyendo JUnit en classpath)..."
$javacArgs = @('-d', $outDir, '-cp', $junitPath) + $javaFiles
& javac @javacArgs
if ($LASTEXITCODE -ne 0) { Write-Error "Compilación fallida (javac). Código: $LASTEXITCODE"; exit $LASTEXITCODE }

Write-Host "Ejecutando JUnit Platform Console..."
& java -jar $junitPath --class-path $outDir --scan-class-path
$exit = $LASTEXITCODE
if ($exit -eq 0) { Write-Host "Pruebas JUnit OK" } else { Write-Error "Pruebas JUnit fallaron. Código: $exit" }
exit $exit
