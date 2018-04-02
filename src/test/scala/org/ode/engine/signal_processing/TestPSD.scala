/** Copyright (C) 2017 Project-ODE
  *
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  */

package org.ode.engine.signal_processing;



import org.ode.utils.test.ErrorMetrics.rmse;
import org.scalatest.{FlatSpec, Matchers};
import scala.math.{cos,sin};

/**
  * Tests for PSD class
  * Author: Alexandre Degurse
  */


class TestPSD extends FlatSpec with Matchers {

  val maxRMSE = 1.1E-15

  "PSD" should "compute the same psd as matlab periodogram on a fake signal" in {
    /** Matlab code
     * s = [0:0.1:10]; s = s(:);
     * sig = 2 * cos(s) + 3 * sin(s);
     * [Pxx,F] = periodogram(sig,[],length(sig),1000);
     */

    val signal: Array[Double] = (0.0 to 10.0 by 0.1).map(x => 2*cos(x) + 3*sin(x)).toArray
    val fs: Double = 1000.0
    val nfft: Int = signal.length
    val normalizationFactor = 1 / (nfft * fs)


    val fftClass: FFT = new FFT(nfft)
    val fft: Array[Double] = fftClass.compute(signal)
    val psdClass: PSD = new PSD(nfft, normalizationFactor)
    val psd: Array[Double] = psdClass.compute(fft)

    val expectedPSD: Array[Double] = Array(
      0.018821131046057503,0.155794411914756625,0.403962223018968447,
      0.036691691896538800,0.013860845848032056,0.007496823811848302,
      0.004775020226013218,0.003339960066646203,0.002483031568184747,
      0.001927224772328257,0.001544815303614363,0.001269789223587577,
      0.001065042235699044,0.000908333515744774,0.000785634526380712,
      0.000687722475070738,0.000608322080169557,0.000543038033676886,
      0.000488714531177963,0.000443036592423639,0.000404273888323465,
      0.000371111524459942,0.000342535516931416,0.000317753597900728,
      0.000296139390379196,0.000277192371903949,0.000260508710992309,
      0.000245759721304290,0.000232675737391845,0.000221033904805966,
      0.000210648833828754,0.000201365373747474,0.000193052975168369,
      0.000185601254086868,0.000178916474315712,0.000172918738164341,
      0.000167539728077807,0.000162720880414618,0.000158411900851216,
      0.000154569551931392,0.000151156659045972,0.000148141293053034,
      0.000145496096845198,0.000143197730168098,0.000141226412423654,
      0.000139565547442531,0.000138201417571006,0.000137122937103952,
      0.000136321457271676,0.000135790616778697,0.000135526233395330
    )

    rmse(psd, expectedPSD) should be < maxRMSE
  }

  it should "compute the same psd as scipy periodogram on a fake signal" in {
    /** Python code:
     * s = numpy.arange(0,10.1,0.1)
     * sig = 2 * numpy.cos(s) + 3 * numpy.sin(s)
     * f,Pxx = scipy.signal.periodogram(sig,1000.0,scaling='spectrum')
     */

    val signal: Array[Double] = (0.0 to 10.0 by 0.1).map(x => 2*cos(x) + 3*sin(x)).toArray
    val fs: Double = 1000.0
    val nfft: Int = signal.length
    // normalizationFactor is specific to scipy
    val normalizationFactor = 1 / fs

    val fftClass: FFT = new FFT(nfft)
    val fft: Array[Double] = fftClass.compute(signal)
    val psdClass: PSD = new PSD(nfft, normalizationFactor)
    val psd: Array[Double] = psdClass.compute(fft)

    val expectedPSD: Array[Double] = Array(
      2.5055478216999097e-32, 1.5425189298490691e+00,
      3.9996259704848351e+00, 3.6328407818354924e-01,
      1.3723609750526911e-01, 7.4225978335131199e-02,
      4.7277427980329106e-02, 3.3068911550952318e-02,
      2.4584470972126325e-02, 1.9081433389388645e-02,
      1.5295201025884829e-02, 1.2572170530570076e-02,
      1.0544972630683536e-02, 8.9934011459879021e-03,
      7.7785596671356870e-03, 6.8091334165420033e-03,
      6.0229908927678039e-03, 5.3766141948207802e-03,
      4.8387577344351767e-03, 4.3865009150856130e-03,
      4.0027117655787401e-03, 3.6743715293064273e-03,
      3.3914407616970456e-03, 3.1460752267400138e-03,
      2.9320731720711333e-03, 2.7444789297421924e-03,
      2.5792941682405701e-03, 2.4332645673693378e-03,
      2.3037201721963600e-03, 2.1884545030295124e-03,
      2.0856320181063346e-03, 1.9937165717573264e-03,
      1.9114155957262716e-03, 1.8376361790780648e-03,
      1.7714502407494719e-03, 1.7120667144985850e-03,
      1.6588091888890023e-03, 1.6110978258874614e-03,
      1.5684346618930775e-03, 1.5303916032812712e-03,
      1.4966005846134332e-03, 1.4667454757727860e-03,
      1.4405554143087035e-03, 1.4177993085951900e-03,
      1.3982813111250954e-03, 1.3818371033915762e-03,
      1.3683308670394837e-03, 1.3576528426135809e-03,
      1.3497173987292706e-03, 1.3444615522645175e-03,
      1.3418438950030705e-03
    )
  }


  it should "raise IllegalArgumentException when given a signal of the wrong length" in {
    val signal: Array[Double] = (0.0 to 10.0 by 0.1).map(cos).toArray
    val psdClass: PSD = new PSD(50, 1.0)

    an [IllegalArgumentException] should be thrownBy psdClass.compute(signal)
  }
}